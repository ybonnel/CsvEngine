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
package fr.ybo.csvengine.validator;

import fr.ybo.csvengine.model.Error;

/**
 * Exception for validation errors<br/>
 * This exception is internal<br/><br/>
 * <i><u>French :</i> Exception permettant de gérer les erreurs de validation.<br/>
 * Exception interne au moteur.
 * 
 * @author ybonnel
 * 
 */
public class ValidationError extends Exception {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Error.
	 */
	private Error error;

	/**
	 * Constructor.
	 * 
	 * @param line
	 *            line in which the error occurred.
	 */
	public ValidationError(String line) {
		error = new Error();
		error.setCsvLine(line);
	}

	/**
	 * @return the validation error occurred.
	 */
	public Error getError() {
		return error;
	}

}
