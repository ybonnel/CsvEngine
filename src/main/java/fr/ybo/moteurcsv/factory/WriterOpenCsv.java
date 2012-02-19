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

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Writer de CSV à base d'open-csv.
 * 
 * @author ybonnel
 * 
 */
public class WriterOpenCsv extends AbstractWriterCsv {

	/**
	 * CSVWriter.
	 */
	private CSVWriter csvWriter;

	/**
	 * Constructeur.
	 * 
	 * @param writer
	 *            fichier CSV.
	 * @param separateur
	 *            séparateur.
	 */
	public WriterOpenCsv(Writer writer, char separateur) {
		this.csvWriter = new CSVWriter(writer, separateur);
	}

	@Override
	public void writeLine(List<String> champs) {
		csvWriter.writeNext(champs.toArray(new String[champs.size()]));
	}

	/**
	 * Fermeture du writer.
	 * 
	 * @throws IOException
	 *             erreur d'entrée sortie.
	 */
	public void close() throws IOException {
		csvWriter.close();
	}
}
