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
package fr.ybo.csvengine.exception;

import java.util.List;

import fr.ybo.csvengine.model.Error;

/**
 * This exception is thrown when the number of errors exceed the number authorised.<br/><br/>
 * <u><i>French :</i></u> Cette exception est déclenchée quand le nombre d'errors atteint dépasse le
 * nombre autorisé.
 * 
 */
public class CsvErrorsExceededException extends Exception {

	/**
     * Maximum size of a message.
	 */
	private static final int TAILLE_MAX_MESSAGE = 4000;
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * List of errors.
	 */
	private List<fr.ybo.csvengine.model.Error> errors;

	/**
	 * Constructor.
	 * 
	 * @param errors
	 *            the errors.
	 */
	public CsvErrorsExceededException(List<Error> errors) {
		super();
		this.errors = errors;
	}

	/**
	 * @return errors.
	 */
	public List<Error> getErrors() {
		return errors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder();
		for (Error error : getErrors()) {
			for (String message : error.getMessages()) {
				builder.append(message);
				builder.append('\n');
			}
			if (builder.length() > TAILLE_MAX_MESSAGE) {
				break;
			}
		}
		return builder.toString();
	}

}
