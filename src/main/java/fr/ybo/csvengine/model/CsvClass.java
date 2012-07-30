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
package fr.ybo.csvengine.model;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import fr.ybo.csvengine.exception.CsvEngineException;

/**
 * Represents a class associate with a CSV File.<br/><br/>
 * <u><i>French :</i></u> Répésente une classe associé à un fichier CSV.
 * 
 * @author ybonnel
 * 
 */
public class CsvClass {

	/**
	 * Separator.
	 */
	private final String separator;

	/**
	 * The class.
	 */
	private final Class<?> clazz;

	/**
	 * The constructor.
	 */
	private Constructor<?> constructor;

	/**
     * Map of the filed in the class.
	 */
	private final Map<String, CsvField> mapOfFields = new HashMap<String, CsvField>();

	/**
     * Map of orders.
	 */
	private final Map<String, Integer> orders = new HashMap<String, Integer>();

	/**
	 * Constructor.
	 * 
	 * @param separator
	 *            separator.
	 * @param clazz
	 *            the class.
	 */
	public CsvClass(String separator, Class<?> clazz) {
		this.separator = separator;
		this.clazz = clazz;
		try {
			constructor = clazz.getDeclaredConstructor((Class<?>[]) null);
            constructor.setAccessible(true);
		} catch (Exception e) {
            throw new CsvEngineException("Error while getting the default constructor of " + clazz.getSimpleName()
                    + ", this class miss a constructor without parameters", e);
		}
	}

	/**
     * Get the CsvField associated with the column name.
	 * 
	 * @param columnName
	 *            name of column.
	 * @return the field.
	 */
	public CsvField getCsvField(String columnName) {
		return mapOfFields.get(columnName);
	}

	/**
	 * @return the list of column names.
	 */
	public Iterable<String> getColumnNames() {
		return mapOfFields.keySet();
	}

	/**
	 * @return the class.
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * @return the constructor.
	 */
	public Constructor<?> getConstructor() {
		return constructor;
	}
	
	/**
     * Separator without escape.
	 */
	private String separatorWithoutEscape = null;

	/**
	 * @return Separator without escape.
	 */
	public char getSeparatorWithoutEscape() {
		if (separatorWithoutEscape == null) {
			separatorWithoutEscape = separator.replaceAll("\\\\", "");
		}
		if (separatorWithoutEscape.length() != 1) {
			throw new CsvEngineException("The separator " + separatorWithoutEscape + " contains more than 1 character.");
		}
		return separatorWithoutEscape.charAt(0);
		
	}

	/**
	 * @param columnName
	 *            name of the column.
	 * @param csvField
	 *            CSV Field.
	 */
	public void setCsvField(String columnName, CsvField csvField) {
		mapOfFields.put(columnName, csvField);
	}

	/**
	 * Add of the order.
	 * 
	 * @param columnName
	 *            name of the column.
	 * @param order
	 *            order.
	 */
	public void putOrder(String columnName, int order) {
        orders.put(columnName, order);
	}

	/**
     * Get the order of a field.
	 * 
	 * @param columnName
	 *            name of the column.
	 * @return the order.
	 */
	public int getOrder(String columnName) {
		return orders.get(columnName);
	}

}
