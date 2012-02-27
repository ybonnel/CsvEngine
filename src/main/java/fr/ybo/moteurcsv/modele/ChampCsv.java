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
import java.util.HashMap;
import java.util.Map;

import fr.ybo.moteurcsv.adapter.AdapterCsv;
import fr.ybo.moteurcsv.exception.MoteurCsvException;
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
	private final Class<? extends AdapterCsv<?>> adapter;

	/**
	 * Validator à utiliser.
	 */
	private final Class<? extends ValidatorCsv> validator;

	/**
	 * Map des adapteurs, permet de ne créer qu'une instance par adapter.
	 */
	private static final Map<Class<? extends AdapterCsv<?>>, AdapterCsv<?>> MAP_ADAPTERS =
			new HashMap<Class<? extends AdapterCsv<?>>, AdapterCsv<?>>();

	/**
	 * Map des validateurs, permet de ne créer qu'une instance par validateur.
	 */
	private static final Map<Class<? extends ValidatorCsv>, ValidatorCsv> MAP_VALIDATOR =
			new HashMap<Class<? extends ValidatorCsv>, ValidatorCsv>();

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
	public ChampCsv(Class<? extends AdapterCsv<?>> adapter, Class<? extends ValidatorCsv> validator, Field field,
			boolean obligatoire) {
		this.adapter = adapter;
		this.field = field;
		this.obligatoire = obligatoire;
		this.validator = validator;
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
	public AdapterCsv<Object> getNewAdapterCsv() {
		if (!MAP_ADAPTERS.containsKey(adapter)) {
			try {
				Constructor<? extends AdapterCsv<?>> construteur = adapter.getConstructor((Class<?>[]) null);
				MAP_ADAPTERS.put(adapter, construteur.newInstance((Object[]) null));
			} catch (Exception exception) {
				throw new MoteurCsvException(exception);
			}
		}
		return (AdapterCsv<Object>) MAP_ADAPTERS.get(adapter);
	}

	/**
	 * Construit un nouveau validateur seulement si on en a pas déjà créer un.
	 * 
	 * @return le validateur à utiliser.
	 */
	public ValidatorCsv getNewValidatorCsv() {
		if (validator == null) {
			return null;
		}
		if (!MAP_VALIDATOR.containsKey(validator)) {
			try {
				Constructor<? extends ValidatorCsv> construteur = validator.getConstructor((Class<?>[]) null);
				MAP_VALIDATOR.put(validator, construteur.newInstance((Object[]) null));
			} catch (Exception exception) {
				throw new MoteurCsvException(exception);
			}
		}
		return MAP_VALIDATOR.get(validator);
	}
}
