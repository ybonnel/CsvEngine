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
package fr.ybo.csvengine.adapter;

/**
 * <h1>Adapter for Boolean.</h1>
 * <ul>
 * <li>Boolean.TRUE = "1" in CSV.</li>
 * <li>Boolean.FALSE = "0" in CSV.</li>
 * </ul><br/>
 *
 * <u><i>French :</i></u> <h1>Adapteur pour les Boolean.</h1>
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
	 * Transform a string into Boolean.
	 * 
	 * @param string
	 *            the string to transform.
	 * @return Boolean.TRUE if string = "1".
	 */
	public Boolean parse(String string) {
		return Integer.parseInt(string) == 1;
	}

	/**
	 * Transform a Boolean into String.
	 * 
	 * @param object
	 *            the Boolean.
	 * @return the resulting String with this rule :
	 *         <ul>
	 *         <li>Boolean.TRUE -> "1"</li>
	 *         <li>Boolean.FALSE -> "0"</li>
	 *         </ul>
	 */
	public String toString(Boolean object) {
		return object ? "1" : "0";
	}
}
