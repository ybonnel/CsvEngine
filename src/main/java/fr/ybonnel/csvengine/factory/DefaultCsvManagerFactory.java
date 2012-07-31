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
package fr.ybonnel.csvengine.factory;

import java.io.Reader;
import java.io.Writer;

/**
 * Default factory. Provied implementations based on opencsv.<br/><br/>
 * <i><u>French :</i> Factory par défaut. Fournit des implémentations à base d'opencsv.
 * 
 * @author ybonnel
 * 
 */
public class DefaultCsvManagerFactory implements CsvManagerFactory {

	/**
     * CsvReader creation.
	 * 
	 * @param reader
	 *            reader which represent the CSV File.
	 * @param separator
	 *            separator of fields.
	 * @return CsvReader based on OpenCsv {@link OpenCsvReader}.
	 */
	public AbstractCsvReader createReaderCsv(Reader reader, char separator) {
		return new OpenCsvReader(reader, separator);
	}

	/**
     * CsvWriter creation.
	 * 
	 * @param writer
	 *            writer which represent the CSV File to write.
	 * @param separator
	 *            separator.
	 * @param addQuoteCar
	 *            true to write car to quote elements.
	 * @return CsvWriter based on OpenCsv {@link OpenCsvWriter}.
	 */
	public AbstractCsvWriter createWriterCsv(Writer writer, char separator, boolean addQuoteCar) {
		return new OpenCsvWriter(writer, separator, addQuoteCar);
	}

}
