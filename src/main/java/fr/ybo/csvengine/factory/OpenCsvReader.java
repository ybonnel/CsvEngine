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
import java.io.Reader;

import au.com.bytecode.opencsv.CSVReader;

/**
 * CsvReader based on OpenCsv.<br/><br/>
 * <i><u>French :</i> Reader de CSV à base d'OpenCSV.
 * 
 * @author ybonnel
 * 
 */
public class OpenCsvReader extends AbstractCsvReader {
	
	/**
	 * CSVReader.
	 */
	private CSVReader reader;
	
	/**
	 * Constructor.
	 *
     * @param reader
     *            reader which represent the CSV File.
     * @param separator
     *            separator of fields.
	 */
	public OpenCsvReader(Reader reader, char separator) {
		super();
		this.reader = new CSVReader(reader, separator);
	}
	
	/**
	 * Used to know if a line is empty or note.
	 * 
	 * @param nextLine
	 *            the line to verify.
	 * @return true if the line is empty.
	 */
	protected static boolean isEmpty(String[] nextLine) {
		return (nextLine != null && nextLine.length == 1 && "".equals(nextLine[0]));
	}

	@Override
	public String[] readLine() throws IOException {
		String[] nextLine = reader.readNext();
		if (isEmpty(nextLine)) {
			nextLine = readLine();
		}
		return nextLine;
	}

	/**
	 * Close the reader.
	 * 
	 * @throws IOException
	 *             input/output error.
	 */
	public void close() throws IOException {
		reader.close();
	}
}
