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
package fr.ybo.moteurcsv.validator;

/**
 * Exception à renvoyer en cas de problème de Validation. {@link ValidatorCsv}
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
	 * Constructeur.
	 * 
	 * @param message
	 *            un message clair (celui-ci sera affiché dans les raisons des
	 *            rejets).
	 * @param cause
	 *            cause si l'erreur de validation provient d'une exception.
	 */
	public ValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructeur.
	 * 
	 * @param message
	 *            un message clair (celui-ci sera affiché dans les raisons des
	 *            rejets).
	 */
	public ValidateException(String message) {
		super(message);
	}
}
