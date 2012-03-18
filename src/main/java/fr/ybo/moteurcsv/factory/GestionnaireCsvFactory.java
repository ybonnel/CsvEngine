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
 * Factory fournissant les Reader et Writer de CSV.
 * 
 * A implémenter si on souhaite utiliser autre chose qu'open-csv.
 * 
 * @author ybonnel
 * 
 */
public interface GestionnaireCsvFactory {

	/**
	 * Création d'un writer de CSV.
	 * 
	 * @param writer
	 *            fichier CSV.
	 * 
	 * @param separator
	 *            séparateur.
	 * @param addQuoteCar
	 *            true pour avoir des délimiteurs de champs.
	 * @return le writer de CSV.
	 */
	AbstractWriterCsv createWriterCsv(Writer writer, char separator, boolean addQuoteCar);

	/**
	 * Création d'un reader de CSV.
	 * 
	 * @param reader
	 *            fichier CSV.
	 * @param separator
	 *            séparateur.
	 * @return le reader de CSV.
	 */
	AbstractReaderCsv createReaderCsv(Reader reader, char separator);

}
