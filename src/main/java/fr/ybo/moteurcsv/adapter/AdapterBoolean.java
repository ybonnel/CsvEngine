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
package fr.ybo.moteurcsv.adapter;

/**
 * Adapteur pour les Boolean.
 * <ul>
 * <li>Un Boolean.TRUE = "1" dans le CSV.</li>
 * <li>Un Boolean.FALSE = "0" dans le CSV.</li>
 * </ul>
 * 
 * @author ybonnel
 * 
 */
public class AdapterBoolean extends AdapterCsv<Boolean> {

	/**
	 * Transforme une chaine en Boolean.
	 * 
	 * @param chaine
	 *            la chaine.
	 * @return Boolean.TRUE si la chaine = "1".
	 */
	public Boolean parse(String chaine) {
		return Integer.parseInt(chaine) == 1;
	}

	/**
	 * Transforme un Boolean en chaine.
	 * 
	 * @param objet
	 *            le Boolean.
	 * @return le Boolean obtenu avec la règle :
	 *         <ul>
	 *         <li>Boolean.TRUE -> 1</li>
	 *         <li>Boolean.FALSE -> 0</li>
	 *         </ul>
	 */
	public String toString(Boolean objet) {
		return objet ? "1" : "0";
	}
}
