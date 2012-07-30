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
package fr.ybo.csvengine.factory;

import java.io.Closeable;
import java.util.List;

/**
 *
 * Writer of CSV File.
 * 
 * @author ybonnel
 * 
 */
public abstract class AbstractCsvWriter implements Closeable {

	/**
     * Write a line into the CSV File.
	 * 
	 * @param fields
	 *            field list to write.
	 */
	public abstract void writeLine(List<String> fields);

}
