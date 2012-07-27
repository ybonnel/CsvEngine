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
package fr.ybo.moteurcsv.factory;

import java.io.Reader;
import java.io.Writer;

/**
 * Factory par défaut. Fournit des implémentations à base d'opencsv.
 * 
 * @author ybonnel
 * 
 */
public class DefaultGestionnaireCsvFactory implements GestionnaireCsvFactory {

	/**
	 * Créaion d'un ReaderCsv.
	 * 
	 * @param reader
	 *            fichier CSV.
	 * @param separator
	 *            separator.
	 * @return ReaderCsv à base d'OpenCsv {@link ReaderOpenCsv}.
	 */
	public AbstractReaderCsv createReaderCsv(Reader reader, char separator) {
		return new ReaderOpenCsv(reader, separator);
	}

	/**
	 * Création d'un WriterCsv.
	 * 
	 * @param writer
	 *            fichier CSV.
	 * @param separator
	 *            séparateur.
	 * @param addQuoteCar
	 *            true pour avoir des délimiteurs de champs.
	 * @return WriterCsv à base d'OpenCsv {@link WriterOpenCsv}.
	 */
	public AbstractWriterCsv createWriterCsv(Writer writer, char separator, boolean addQuoteCar) {
		return new WriterOpenCsv(writer, separator, addQuoteCar);
	}

}
