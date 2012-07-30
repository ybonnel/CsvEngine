/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     ybonnel - initial API and implementation
 */
package fr.ybo.csvengine.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ybo.csvengine.adapter.AdapterCsv;
import fr.ybo.csvengine.annotation.CsvColumn;
import fr.ybo.csvengine.annotation.CsvValidation;
import fr.ybo.csvengine.annotation.CsvValidations;
import fr.ybo.csvengine.exception.CsvEngineException;
import fr.ybo.csvengine.validator.ValidateException;
import fr.ybo.csvengine.validator.ValidatorCsv;

/**
 * Represents a column of the CSV File.<br/><br/>
 * <u><i>French :</i></u> Réprésente une colonne du fichier CSV.
 *
 * @author ybonnel
 */
public class CsvField {

    /**
     * Adapter to use.
     */
    private final AdapterCsv<?> adapter;

    /**
     * Validator to use.
     */
    private final List<ValidatorCsv> validators = new ArrayList<ValidatorCsv>();

    /**
     * Map of adapters, this is useful to create just an instance per adapter.
     */
    private static final Map<ClassWithParamKey<AdapterCsv<?>>, AdapterCsv<?>> MAP_ADAPTERS =
            new HashMap<ClassWithParamKey<AdapterCsv<?>>, AdapterCsv<?>>();

    /**
     * Map of validators, this is useful to create just an instance per validator.
     */
    private static final Map<ClassWithParamKey<ValidatorCsv>, ValidatorCsv> MAP_VALIDATOR =
            new HashMap<ClassWithParamKey<ValidatorCsv>, ValidatorCsv>();

    /**
     * Field of the class to map.
     */
    private final Field field;

    /**
     * True if the column is mandatory.
     */
    private final boolean mandatory;

    /**
     * Constructor.
     *
     * @param column         annotation of the field.
     * @param csvValidations annotation de csvValidations if it's present.
     * @param csvValidation  annotation de csvValidation if it's present.
     * @param field          field of the class to map.
     */
    public CsvField(CsvColumn column, CsvValidations csvValidations, CsvValidation csvValidation, Field field) {
        this.field = field;
        this.mandatory = column.mandatory();
        this.adapter = constructAdapter(column);
        constructValidators(csvValidations, csvValidation);
    }

    /**
     * Construct an adapter (reuse an existing one if the adapter with same parameters already exists).
     *
     * @param column annotation of the field.
     * @return the adapter constructed.
     */
    private static AdapterCsv<?> constructAdapter(CsvColumn column) {
        ClassWithParamKey<AdapterCsv<?>> key = new ClassWithParamKey<AdapterCsv<?>>(column.params(), column.adapter());
        if (!MAP_ADAPTERS.containsKey(key)) {
            try {
                Constructor<? extends AdapterCsv<?>> constructor = column.adapter().getConstructor((Class<?>[]) null);
                AdapterCsv<?> adapter = constructor.newInstance((Object[]) null);
                adapter.addParams(key.getMapParams());
                MAP_ADAPTERS.put(key, adapter);
            } catch (Exception exception) {
                throw new CsvEngineException(exception);
            }
        }
        return MAP_ADAPTERS.get(key);
    }

    /**
     * Fill the list of validators.
     *
     * @param csvValidations container of the CsvValidation annotation.
     * @param csvValidation  CsvValidation annotation.
     */
    private void constructValidators(CsvValidations csvValidations, CsvValidation csvValidation) {
        if (csvValidation != null) {
            validators.add(constructOneValidator(csvValidation));
        }
        if (csvValidations != null) {
            for (CsvValidation oneValidation : csvValidations.value()) {
                validators.add(constructOneValidator(oneValidation));
            }
        }
    }

    /**
     * Construct one validator (reuse an existing one if the validator with same parameters already exists).
     *
     * @param csvValidation CsvValidation annotation.
     * @return the validator constructed.
     */
    private static ValidatorCsv constructOneValidator(CsvValidation csvValidation) {
        ClassWithParamKey<ValidatorCsv> key =
                new ClassWithParamKey<ValidatorCsv>(csvValidation.params(), csvValidation.value());
        if (!MAP_VALIDATOR.containsKey(key)) {
            try {
                Constructor<? extends ValidatorCsv> construteur = csvValidation.value().getConstructor((Class<?>[]) null);
                ValidatorCsv validator = construteur.newInstance((Object[]) null);
                validator.addParams(key.getMapParams());
                MAP_VALIDATOR.put(key, validator);
            } catch (Exception exception) {
                throw new CsvEngineException("Error during construction of Validator "
                        + csvValidation.value().getSimpleName(),
                        exception);
            }
        }
        return MAP_VALIDATOR.get(key);
    }

    /**
     * @return field of the class.
     */
    public Field getField() {
        return field;
    }

    /**
     * @return field mandatory?
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * @return adapter to use.
     */
    @SuppressWarnings("unchecked")
    public AdapterCsv<Object> getAdapterCsv() {
        return (AdapterCsv<Object>) adapter;
    }

    /**
     * Method which validate the value of a field.
     *
     * @param value value to va&lidate.
     * @throws ValidateException throw in case of validation error.
     */
    public void validate(String value) throws ValidateException {
        for (ValidatorCsv validator : validators) {
            validator.validate(value);
        }
    }
}
