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
 * Adapteur pour les Double.
 * 
 * @author ybonnel
 * 
 */
public class AdapterDouble implements AdapterCsv<Double> {

	/**
	 * Transforme une chaine en Double.
	 * 
	 * @param chaine
	 *            la chaine à transformer.
	 * @return le double tranformé.
	 */
	public Double parse(String chaine) {
		return Double.valueOf(chaine);
	}

	/**
	 * Transforme un double en chaine.
	 * 
	 * @param objet
	 *            double à tranformer.
	 * @return la chaine obtenue.
	 */
	public String toString(Double objet) {
		return objet.toString();
	}
}
