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
package fr.ybo.moteurcsv.exception;

/**
 * Exception used by {@link fr.ybo.moteurcsv.MoteurCsv}.
 * 
 * @author ybonnel
 * 
 */
@SuppressWarnings("serial")
public class MoteurCsvException extends RuntimeException {

	/**
	 * @param message
	 *            message of the exception.
	 */
	public MoteurCsvException(String message) {
		super(message);
	}

	/**
	 * @param message
	 *            message of the exception.
	 * @param throwable
	 *            source exception.
	 */
	public MoteurCsvException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * @param throwable
	 *            source exception.
	 */
	public MoteurCsvException(Throwable throwable) {
		super(throwable);
	}

}
