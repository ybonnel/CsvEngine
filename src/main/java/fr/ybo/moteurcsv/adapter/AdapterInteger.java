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
 * Adapteur pour les Integer.
 * 
 * @author ybonnel
 * 
 */
public class AdapterInteger implements AdapterCsv<Integer> {

	/**
	 * Transforme une chaine en Integer.
	 * 
	 * @param chaine
	 *            la chaine à transformer.
	 * @return l'integer tranformé.
	 */
	public Integer parse(String chaine) {
		return Integer.valueOf(chaine);
	}

	/**
	 * Transforme un integer en chaine.
	 * 
	 * @param objet
	 *            integer à tranformer.
	 * @return la chaine obtenue.
	 */
	public String toString(Integer objet) {
		return objet.toString();
	}
}
