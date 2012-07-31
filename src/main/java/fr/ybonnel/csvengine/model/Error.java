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

import java.util.ArrayList;
import java.util.List;

/**
 * Represent an error.<br/><br/>
 * <i><u>French :</i> Classe représentant une erreur.
 * 
 * @author ybonnel
 * 
 */
public class Error {

	/**
	 * Line of the CSV File.
	 */
	private String csvLine;

	/**
	 * @return the line of the CSV File which contain the error.
	 */
	public String getCsvLine() {
		return csvLine;
	}

	/**
	 * @param csvLine
	 *            the line of the CSV File which contain the error.
	 */
	public void setCsvLine(String csvLine) {
		this.csvLine = csvLine;
	}

	/**
     * List of the errors messages.
	 */
	private List<String> messages;

	/**
	 * @return the errors messages.
	 */
	public List<String> getMessages() {
		if (messages == null) {
			messages = new ArrayList<String>();
		}
		return messages;
	}
}
