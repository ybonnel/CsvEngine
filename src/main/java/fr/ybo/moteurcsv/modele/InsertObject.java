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

/**
 * Traitement à effectuer pendant la lecture d'un fichier CSV.
 * 
 * @author ybonnel
 * 
 * @param <Objet>
 */
public interface InsertObject<Objet> {

	/**
	 * Traitement à effectuer pour un enregistrement d'un fichier CSV.
	 * 
	 * @param objet
	 *            objet à traiter représentant un enregistrement.
	 */
	void insertObject(Objet objet);
}