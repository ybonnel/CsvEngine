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

import java.util.Map;

import fr.ybo.csvengine.exception.InvalidParamException;
import fr.ybo.csvengine.validator.ValidateException;

/**
 * Class to extend for all Adapter for CSV.<br/>
 * <u><i>French :</i></u> Class à étendre pour tout les adapteur CSV.
 *
 * @param <T> Class to transform in CSV (associate with a column in CSV).<br/>
 *            <u><i>French :</i></u> Class à adapter en CSV (associé à une colonne).
 * @author ybonnel
 */
public abstract class AdapterCsv<T> {

    /**
     * Method call after the construction of adapter<br/>
     * Method to override if you want use parameters.
     *
     * @param params parameters.
     * @throws fr.ybo.csvengine.exception.InvalidParamException if parameters are incorrect.
     */
    public void addParams(Map<String, String> params) throws InvalidParamException {
    }

    /**
     * Transform a String into T.
     *
     * @param string the string to transform.
     * @return the object transformed.
     * @throws ValidateException can be throw if the string haven't good format.
     */
    public abstract T parse(String string) throws ValidateException;

    /**
     * Transform a T into String.
     *
     * @param object object to transform.
     * @return the resulting string.
     */
    public abstract String toString(T object);
}
