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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of motor.<br/>
 * Contains :
 * <ul>
 * <li>The list of transformed objects.</li>
 * <li>The list of errors occurred.</li>
 * </ul><br/><br/>
 * <u><i>French :</i></u> Classe représentant le résultat du moteur.<br/>
 * Contient :
 * <ul>
 * <li>La liste de objects transormés.</li>
 * <li>La liste des errors rencontrées.</li>
 * </ul>
 * 
 * @param <T>
 *            Object contains in the result.
 * 
 */
public class Result<T> {

	/**
     * The transformed objects.
	 */
	private List<T> objects;

	/**
	 * Errors occurred.
	 */
	private List<Error> errors;

	/**
	 * @return the transformed objects.
	 */
	public List<T> getObjects() {
		if (objects == null) {
			objects = new ArrayList<T>();
		}
		return objects;
	}

	/**
	 * @return errors occurred.
	 */
	public List<Error> getErrors() {
		if (errors == null) {
			errors = new ArrayList<Error>();
		}
		return errors;
	}
}
