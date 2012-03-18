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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fr.ybo.moteurcsv.adapter.AdapterCsv;
import fr.ybo.moteurcsv.annotation.BaliseCsv;
import fr.ybo.moteurcsv.annotation.Param;
import fr.ybo.moteurcsv.annotation.Validation;
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
	private final ValidatorCsv validator;

	public static class Parametre {
		private String name;
		private String value;

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public Parametre(Param param) {
			name = param.name();
			value = param.value();
		}

		public static Parametre[] paramsToParametres(Param[] params) {
			Parametre[] parametres = new Parametre[params.length];
			for (int count = 0; count < params.length; count++) {
				parametres[count] = new Parametre(params[count]);
			}
			return parametres;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Parametre other = (Parametre) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
	}

	public static class ClassWithParamKey<T> {
		private Parametre[] params;
		private Class<? extends T> clazz;

		public ClassWithParamKey(Param[] params, Class<? extends T> clazz) {
			this.params = Parametre.paramsToParametres(params);
			this.clazz = clazz;
		}

		public Map<String, String> getMapParams() {
			Map<String, String> mapParams = new HashMap<String, String>();
			for (Parametre parametre : params) {
				mapParams.put(parametre.getName(), parametre.getValue());
			}
			return mapParams;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
			result = prime * result + Arrays.hashCode(params);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ClassWithParamKey<?> other = (ClassWithParamKey<?>) obj;
			if (clazz == null) {
				if (other.clazz != null)
					return false;
			} else if (!clazz.equals(other.clazz))
				return false;
			if (!Arrays.equals(params, other.params))
				return false;
			return true;
		}
	}

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
	 * @param adapter
	 *            adapter à utiliser.
	 * @param field
	 *            attribut de la classe à mapper.
	 * 
	 * @param validator
	 *            validator à utiliser.
	 * @param obligatoire
	 *            true si le champ est obligatoire.
	 */
	public ChampCsv(BaliseCsv balise, Validation validation, Field field) {
		this.field = field;
		this.obligatoire = balise.obligatoire();
		this.adapter = constructAdapter(balise);
		this.validator = constructValidator(validation);
	}

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

	private static ValidatorCsv constructValidator(Validation validation) {
		if (validation == null) {
			return null;
		}
		ClassWithParamKey<ValidatorCsv> key =
				new ClassWithParamKey<ValidatorCsv>(validation.params(), validation.value());
		if (!MAP_VALIDATOR.containsKey(key)) {
			try {
				Constructor<? extends ValidatorCsv> construteur = validation.value().getConstructor((Class<?>[]) null);
				ValidatorCsv validator = construteur.newInstance((Object[]) null);
				validator.addParams(key.getMapParams());
				MAP_VALIDATOR.put(key, validator);
			} catch (Exception exception) {
				throw new MoteurCsvException(exception);
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
		if (validator != null) {
			validator.validate(valeur);
		}
	}
}
