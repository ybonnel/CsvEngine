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

import java.util.Map;

import fr.ybo.moteurcsv.exception.InvalidParamException;

/**
 * Email validator.<br/>
 * Pattern used : "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$".<br/><br/>
 * <u><i>French :</i></u> Validateur d'email.<br/>
 * Pattern utilisé : "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$".
 */
public class ValidatorEmail extends ValidatorRegExp {

	/**
	 * Pattern for mails.
	 */
	private static final String PATTERN_EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ybo.moteurcsv.validator.ValidatorRegExp#addParams(java.util.Map)
	 */
	@Override
	public void addParams(Map<String, String> params) throws InvalidParamException {
		params.put(PARAM_PATTERN, PATTERN_EMAIL);
		super.addParams(params);
	}

}
