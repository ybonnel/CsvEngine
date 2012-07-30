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

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * CsvWriter based on open-csv.<br/><br/>
 * <i><u>French :</i> Writer de CSV à base d'open-csv.
 * 
 * @author ybonnel
 * 
 */
public class OpenCsvWriter extends AbstractCsvWriter {

	/**
	 * CSVWriter.
	 */
	private CSVWriter csvWriter;

	/**
     * @param writer
     *            writer which represent the CSV File to write.
     * @param separator
     *            separator.
     * @param addQuoteCar
     *            true to write car to quote elements.
	 */
	public OpenCsvWriter(Writer writer, char separator, boolean addQuoteCar) {
		if (addQuoteCar) {
			this.csvWriter = new CSVWriter(writer, separator);
		} else {
			this.csvWriter = new CSVWriter(writer, separator, CSVWriter.NO_QUOTE_CHARACTER);
		}
	}

	@Override
	public void writeLine(List<String> fields) {
		csvWriter.writeNext(fields.toArray(new String[fields.size()]));
	}

	/**
	 * Close the writer.
	 * 
	 * @throws IOException
	 *             input/ouput error.
	 */
	public void close() throws IOException {
		csvWriter.close();
	}
}
