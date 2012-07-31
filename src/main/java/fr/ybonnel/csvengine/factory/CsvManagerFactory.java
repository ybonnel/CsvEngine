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
 * Factory which provide {@link AbstractCsvReader} and {@link AbstractCsvWriter}.<br/>
 * You must implement it if you want don't want to use open-csv<br/><br/>
 * <i><u>French :</i> Factory fournissant les Reader et Writer de CSV.<br/>
 * A implémenter si on souhaite utiliser autre chose qu'open-csv.
 * 
 * @author ybonnel
 * 
 */
public interface CsvManagerFactory {

    /**
     * CsvWriter creation.
     *
     * @param writer
     *            writer which represent the CSV File to write.
     * @param separator
     *            separator.
     * @param addQuoteCar
     *            true to write car to quote elements.
     * @return the CsvWriter.
     */
	AbstractCsvWriter createWriterCsv(Writer writer, char separator, boolean addQuoteCar);

    /**
     * CsvReader creation.
     *
     * @param reader
     *            reader which represent the CSV File.
     * @param separator
     *            separator of fields.
     * @return the CsvReader.
     */
	AbstractCsvReader createReaderCsv(Reader reader, char separator);

}
