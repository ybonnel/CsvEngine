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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fr.ybo.moteurcsv.annotation.Param;

/**
 * Classe permettant de repréter la clé contenant une classe et ses paramètres.
 * (utilisé pour les Adapter et la Validateur.
 * 
 * @param <T>
 *            Classe concernée.
 */
public class ClassWithParamKey<T> {
	/**
	 * Paramètres de la classe.
	 */
	private Parametre[] params;
	/**
	 * Classe concernée.
	 */
	private Class<? extends T> clazz;

	/**
	 * Constructeur.
	 * 
	 * @param params
	 *            paramètres.
	 * @param clazz
	 *            classe concernée.
	 */
	public ClassWithParamKey(Param[] params, Class<? extends T> clazz) {
		this.params = Parametre.paramsToParametres(params);
		this.clazz = clazz;
	}

	/**
	 * Permet de récupérer les paramètres sous forme de Map <Nom, Valeur>.
	 * 
	 * @return la map construite.
	 */
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ClassWithParamKey<?> other = (ClassWithParamKey<?>) obj;
		if (clazz == null) {
			if (other.clazz != null) {
				return false;
			}
		} else if (!clazz.equals(other.clazz)) {
			return false;
		}
		if (!Arrays.equals(params, other.params)) {
			return false;
		}
		return true;
	}
}
