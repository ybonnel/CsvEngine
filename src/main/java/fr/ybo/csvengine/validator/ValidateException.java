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

/**
 * Exception to throw for a validation error. {@link ValidatorCsv}<br/><br/>
 * <i><u>French :</i> Exception à renvoyer en cas de problème de validation. {@link ValidatorCsv}
 * 
 * @author ybonnel
 * 
 */
public class ValidateException extends Exception {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param message
     *            a message (this will be display in the reason of the validation error).
	 * @param cause
	 *            cause if the error is associated with a exception.
	 */
	public ValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor.
	 * 
	 * @param message
     *            a message (this will be display in the reason of the validation error).
	 */
	public ValidateException(String message) {
		super(message);
	}
}
