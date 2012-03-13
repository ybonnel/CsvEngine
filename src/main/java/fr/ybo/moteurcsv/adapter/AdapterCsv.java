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
 * Interface à implémenter pour tout les adapter CSV.
 * 
 * @author ybonnel
 * 
 * @param <Objet>
 *            Objet à adapter en CSV (associé à une colonne).
 */
public abstract class AdapterCsv<Objet> {

	/**
	 * Transforme une chaine en Objet.
	 * 
	 * @param chaine
	 *            la chaine à transformer.
	 * @return l'objet tranformé.
	 */
	public abstract Objet parse(String chaine);

	/**
	 * Transforme un objet en chaine.
	 * 
	 * @param objet
	 *            objet à tranformer.
	 * @return la chaine obtenu.
	 */
	public abstract String toString(Objet objet);
}
