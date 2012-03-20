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
import fr.ybo.moteurcsv.annotation.BaliseCsv;
import fr.ybo.moteurcsv.annotation.Validation;
import fr.ybo.moteurcsv.annotation.Validations;
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
	 * Permet de savoir si le champ est obligatoire ou non.
	 */
	private final boolean obligatoire;

	/**
	 * Constructeur.
	 * 
	 * @param balise
	 *            annotation du champ.
	 * @param validation
	 *            annotation de validation si elle est présente.
	 * @param field
	 *            attribut de la classe à mapper.
	 */
	public ChampCsv(BaliseCsv balise, Validations validations, Validation validation, Field field) {
		this.field = field;
		this.obligatoire = balise.obligatoire();
		this.adapter = constructAdapter(balise);
		constructValidators(validations, validation);
	}

	/**
	 * Construit un adapter (en réutilisant un existant si l'adapter avec les
	 * mêmes paramètres existe déjà).
	 * 
	 * @param balise
	 *            annotation du champ.
	 * @return l'adapter construit.
	 */
	private static AdapterCsv<?> constructAdapter(BaliseCsv balise) {
		ClassWithParamKey<AdapterCsv<?>> key = new ClassWithParamKey<AdapterCsv<?>>(balise.params(), balise.adapter());
		if (!MAP_ADAPTERS.containsKey(key)) {
			try {
				Constructor<? extends AdapterCsv<?>> construteur = balise.adapter().getConstructor((Class<?>[]) null);
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
	 * @param validations
	 *            conteneur d'annotation de validation.
	 * @param validation
	 *            annotation de validation.
	 * @return le validator construit.
	 */
	private void constructValidators(Validations validations, Validation validation) {
		if (validation != null) {
			validators.add(constructOneValidator(validation));
		}
		if (validations != null) {
			for (Validation oneValidation : validations.value()) {
				validators.add(constructOneValidator(oneValidation));
			}
		}
	}

	/**
	 * Construit un validator (en réutilisant un existant si le validator avec
	 * les mêmes paramètres existe déjà).
	 * 
	 * @param validation
	 *            annotation de validation.
	 * @return le validator construit.
	 */
	private static ValidatorCsv constructOneValidator(Validation validation) {
		ClassWithParamKey<ValidatorCsv> key =
				new ClassWithParamKey<ValidatorCsv>(validation.params(), validation.value());
		if (!MAP_VALIDATOR.containsKey(key)) {
			try {
				Constructor<? extends ValidatorCsv> construteur = validation.value().getConstructor((Class<?>[]) null);
				ValidatorCsv validator = construteur.newInstance((Object[]) null);
				validator.addParams(key.getMapParams());
				MAP_VALIDATOR.put(key, validator);
			} catch (Exception exception) {
				throw new MoteurCsvException("Problème lors de la construction du validateur "
						+ validation.value().getSimpleName(),
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
	 * @return champ obligatoire?
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
