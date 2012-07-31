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
package fr.ybonnel.csvengine.model;

import fr.ybonnel.csvengine.annotation.CsvParam;

/**
 * Represents a parameter with a name and a value.<br/><br/>
 * <i><u>French :</i> Classe représentant un paramètre avec un nom et une valeur.
 * 
 */
public class Parameter {
	/**
	 * Name of the parameter.
	 */
	private String name;
	/**
	 * Value of the parameter.
	 */
	private String value;

	/**
	 * @return name of the parameter.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return value of the parameter.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Constructor.
	 * 
	 * @param param
	 *            parameter in annotation format.
	 */
	public Parameter(CsvParam param) {
		name = param.name();
		value = param.value();
	}

	/**
     * Convert a list of {@link fr.ybonnel.csvengine.annotation.CsvParam} into a list of {@link Parameter}.
	 * 
	 * @param params
	 *            the CsvParam to convert.
	 * @return the Parameters converted.
	 */
	public static Parameter[] paramsToParameters(CsvParam[] params) {
		Parameter[] parameters = new Parameter[params.length];
		for (int count = 0; count < params.length; count++) {
			parameters[count] = new Parameter(params[count]);
		}
		return parameters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + name.hashCode();
		result = prime * result + value.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Parameter other = (Parameter) obj;
		return name.equals(other.name) && value.equals(other.value);
	}
}