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

import java.util.List;

/**
 * Implémentation de InsertObjet permettant d'insérer dans une liste.
 * 
 * @author ybonnel
 * 
 * @param <Objet>
 *            objet à insérer.
 */
public class InsertInList<Objet> implements InsertObject<Objet> {

	/**
	 * Liste dans laquelle il faut insérer les objets.
	 */
	private List<Objet> objets;

	/**
	 * Constructeur.
	 * 
	 * @param objets
	 *            liste dans laquelle il faut insérer les objets.
	 */
	public InsertInList(List<Objet> objets) {
		this.objets = objets;
	}

	/**
	 * Insertion de l'objet.
	 * 
	 * @param objet
	 *            à insérer.
	 */
	public void insertObject(Objet objet) {
		objets.add(objet);
	}
}