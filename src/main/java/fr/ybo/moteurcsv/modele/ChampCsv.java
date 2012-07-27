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
package fr.ybo.moteurcsv.modele;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ybo.moteurcsv.adapter.AdapterCsv;
import fr.ybo.moteurcsv.annotation.CsvColumn;
import fr.ybo.moteurcsv.annotation.CsvValidation;
import fr.ybo.moteurcsv.annotation.CsvValidations;
import fr.ybo.moteurcsv.exception.MoteurCsvException;
import fr.ybo.moteurcsv.validator.ValidateException;
import fr.ybo.moteurcsv.validator.ValidatorCsv;

/**
 * Réprésente une colonne du fichier CSV.
 * 
 * @author ybonnel
 * 
 */
public class ChampCsv {

	/**
	 * Adapter à utiliser.
	 */
	private final AdapterCsv<?> adapter;

	/**
	 * Validator à utiliser.
	 */
	private final List<ValidatorCsv> validators = new ArrayList<ValidatorCsv>();

	/**
	 * Map des adapteurs, permet de ne créer qu'une instance par adapter.
	 */
	private static final Map<ClassWithParamKey<AdapterCsv<?>>, AdapterCsv<?>> MAP_ADAPTERS =
			new HashMap<ClassWithParamKey<AdapterCsv<?>>, AdapterCsv<?>>();

	/**
	 * Map des validateurs, permet de ne créer qu'une instance par validateur.
	 */
	private static final Map<ClassWithParamKey<ValidatorCsv>, ValidatorCsv> MAP_VALIDATOR =
			new HashMap<ClassWithParamKey<ValidatorCsv>, ValidatorCsv>();

	/**
	 * Attribut de la classe à mapper.
	 */
	private final Field field;

	/**
	 * Permet de savoir si le champ est mandatory ou non.
	 */
	private final boolean obligatoire;

	/**
	 * Constructeur.
	 * 
	 * @param column
	 *            annotation du champ.
	 * @param csvValidation
	 *            annotation de csvValidation si elle est présente.
	 * @param field
	 *            attribut de la classe à mapper.
	 */
	public ChampCsv(CsvColumn column, CsvValidations csvValidations, CsvValidation csvValidation, Field field) {
		this.field = field;
		this.obligatoire = column.mandatory();
		this.adapter = constructAdapter(column);
		constructValidators(csvValidations, csvValidation);
	}

	/**
	 * Construit un adapter (en réutilisant un existant si l'adapter avec les
	 * mêmes paramètres existe déjà).
	 * 
	 * @param column
	 *            annotation du champ.
	 * @return l'adapter construit.
	 */
	private static AdapterCsv<?> constructAdapter(CsvColumn column) {
		ClassWithParamKey<AdapterCsv<?>> key = new ClassWithParamKey<AdapterCsv<?>>(column.params(), column.adapter());
		if (!MAP_ADAPTERS.containsKey(key)) {
			try {
				Constructor<? extends AdapterCsv<?>> construteur = column.adapter().getConstructor((Class<?>[]) null);
				AdapterCsv<?> adapter = construteur.newInstance((Object[]) null);
				adapter.addParams(key.getMapParams());
				MAP_ADAPTERS.put(key, adapter);
			} catch (Exception exception) {
				throw new MoteurCsvException(exception);
			}
		}
		return MAP_ADAPTERS.get(key);
	}

	/**
	 * Remplit la liste des Validator à construire.
	 * 
	 * @param csvValidations
	 *            conteneur d'annotation de csvValidation.
	 * @param csvValidation
	 *            annotation de csvValidation.
	 * @return le validator construit.
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
	 * Construit un validator (en réutilisant un existant si le validator avec
	 * les mêmes paramètres existe déjà).
	 * 
	 * @param csvValidation
	 *            annotation de csvValidation.
	 * @return le validator construit.
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
				throw new MoteurCsvException("Problème lors de la construction du validateur "
						+ csvValidation.value().getSimpleName(),
						exception);
			}
		}
		return MAP_VALIDATOR.get(key);
	}

	/**
	 * @return attribut de la classe.
	 */
	public Field getField() {
		return field;
	}

	/**
	 * @return champ mandatory?
	 */
	public boolean isObligatoire() {
		return obligatoire;
	}

	/**
	 * Construit un nouvel adapter seulement si on en a pas déjà créer un.
	 * 
	 * @return l'adapter à utiliser.
	 */
	@SuppressWarnings("unchecked")
	public AdapterCsv<Object> getAdapterCsv() {
		return (AdapterCsv<Object>) adapter;
	}

	/**
	 * Méthode permettant de valider la valeur d'un champ.
	 * 
	 * @param valeur
	 *            valeur à valider.
	 * @throws ValidateException
	 *             renvoyée en cas d'erreur de validation.
	 */
	public void validate(String valeur) throws ValidateException {
		for (ValidatorCsv validator : validators) {
			validator.validate(valeur);
		}
	}
}
