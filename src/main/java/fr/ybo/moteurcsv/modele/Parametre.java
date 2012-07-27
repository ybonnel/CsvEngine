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

import fr.ybo.moteurcsv.annotation.CsvParam;

/**
 * Classe représentant un paramètre avec un nom et une valeur.
 * 
 */
public class Parametre {
	/**
	 * Nom du paramètre.
	 */
	private String name;
	/**
	 * Valeur du paramètre.
	 */
	private String value;

	/**
	 * @return nom du paramètre.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return valeur du paramètre.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Construteur.
	 * 
	 * @param param
	 *            paramètre sous la format de l'annotation.
	 */
	public Parametre(CsvParam param) {
		name = param.name();
		value = param.value();
	}

	/**
	 * Permet de convertir une liste de {@link fr.ybo.moteurcsv.annotation.CsvParam} en liste de
	 * {@link Parametre}.
	 * 
	 * @param params
	 *            les CsvParam à convertir.
	 * @return les Parametres convertis.
	 */
	public static Parametre[] paramsToParametres(CsvParam[] params) {
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
		result = prime * result + name.hashCode();
		result = prime * result + value.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Parametre other = (Parametre) obj;
		return name.equals(other.name) && value.equals(other.value);
	}
}