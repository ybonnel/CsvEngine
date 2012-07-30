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
package fr.ybo.moteurcsv.model;

import java.util.List;

/**
 * Implementation of InsertObject which insert the Object in a list.<br/><br/>
 * <u><i>French :</i></u> Implémentation de InsertObjet permettant d'insérer dans une liste.
 * 
 * @author ybonnel
 * 
 * @param <T>
 *            object to insert.
 */
public class InsertInList<T> implements InsertObject<T> {

	/**
     * List in which we must insert objects.
	 */
	private List<T> objects;

	/**
	 * Constructor.
	 * 
	 * @param objects
	 *            List in which we must insert objects.
	 */
	public InsertInList(List<T> objects) {
		this.objects = objects;
	}

	/**
	 * Insert of the object.
	 * 
	 * @param object
	 *            to insert.
	 */
	public void insertObject(T object) {
        objects.add(object);
	}
}