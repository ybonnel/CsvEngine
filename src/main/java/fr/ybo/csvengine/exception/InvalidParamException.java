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

/**
 * Exception throw if parameters of Adapter of Validater are wrong.
 * <u><i>French :</i></u> Exception envoyée si les paramètres des adapteurs ou des validateur sont
 * mauvais.
 */
@SuppressWarnings("serial")
public class InvalidParamException extends Exception {

	/**
	 * Constructor with a message.
	 * 
	 * @param message
	 *            the message.
	 */
	public InvalidParamException(String message) {
		super(message);
	}

	/**
	 * Constructor with a message and an exception.
	 * 
	 * @param message
	 *            the message.
	 * @param exception
	 *            the exception.
	 */
	public InvalidParamException(String message, Exception exception) {
		super(message, exception);
	}
}
