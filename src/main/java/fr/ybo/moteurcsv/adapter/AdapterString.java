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
 * Adapter de String, adapter par défaut qui ne fait rien.
 * 
 * @author ybonnel
 * 
 */
public class AdapterString extends AdapterCsv<String> {

	/**
	 * Ne fait rien.
	 * 
	 * @param chaine
	 *            chaine à retourner.
	 * @return chaine passée en paramètre.
	 */
	public String parse(String chaine) {
		return chaine;
	}

	/**
	 * Ne fait rien.
	 * 
	 * @param objet
	 *            objet à retourner.
	 * @return objet passé en paramètre.
	 */
	public String toString(String objet) {
		return objet;
	}
}
