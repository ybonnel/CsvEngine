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
 * Adapter for String, default adapter which do nothing.<br/><br/>
 * <i><u>French :</i> Adapteur de String, adapteur par défaut qui ne fait rien.
 *
 * @author ybonnel
 */
public class AdapterString extends AdapterCsv<String> {

    /**
     * Transform a String into String.
     *
     * @param string the string to transform.
     * @return the String transformed.
     */
    public String parse(String string) {
        return string;
    }

    /**
     * Transform a String into String.
     *
     * @param object String to transform.
     * @return the resulting string.
     */
    public String toString(String object) {
        return object;
    }
}
