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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fr.ybo.moteurcsv.annotation.CsvParam;

/**
 * Represents a key containing the class with its parameters (use for Adapters and Validators).<br/><br/>
 * <u><i>French :</i></u> Classe permettant de repréter la clé contenant une classe et ses paramètres.
 * (utilisé pour les Adapter et la Validateur.
 *
 * @param <T> the class.
 */
public class ClassWithParamKey<T> {
    /**
     * Parameters of the class.
     */
    private Parameter[] params;
    /**
     * The class.
     */
    private Class<? extends T> clazz;

    /**
     * Constructor.
     *
     * @param params parameters.
     * @param clazz  the class.
     */
    public ClassWithParamKey(CsvParam[] params, Class<? extends T> clazz) {
        this.params = Parameter.paramsToParameters(params);
        this.clazz = clazz;
    }

    /**
     * Get the parameters into a Map&lt;Name, Value&gt;.
     *
     * @return the resulting map.
     */
    public Map<String, String> getMapParams() {
        Map<String, String> mapParams = new HashMap<String, String>();
        for (Parameter parameter : params) {
            mapParams.put(parameter.getName(), parameter.getValue());
        }
        return mapParams;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + clazz.hashCode();
        result = prime * result + Arrays.hashCode(params);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        ClassWithParamKey<?> other = (ClassWithParamKey<?>) obj;
        return (clazz.equals(other.clazz) && Arrays.equals(params, other.params));
    }
}
