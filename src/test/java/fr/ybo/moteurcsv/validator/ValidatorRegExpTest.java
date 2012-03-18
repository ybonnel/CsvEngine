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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import fr.ybo.moteurcsv.exception.InvalideParamException;

/**
 * @author ybonnel
 *
 */
public class ValidatorRegExpTest {

	@Test
	public void testParametreAbsent() {
		ValidatorRegExp validator = new ValidatorRegExp();
		try {
			validator.addParams(new HashMap<String, String>());
			fail("Une exception aurait du être levée");
		} catch (InvalideParamException exception) {
			assertTrue(exception.getMessage().contains(ValidatorRegExp.PARAM_PATTERN));
			assertTrue(exception.getMessage().contains("obligatoire"));
		}
	}

	@Test
	public void testParametreIncorrect() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(ValidatorRegExp.PARAM_PATTERN, "{[(");
		ValidatorRegExp validator = new ValidatorRegExp();
		try {
			validator.addParams(params);
			fail("Une exception aurait du être levée");
		} catch (InvalideParamException exception) {
			assertTrue(exception.getMessage().contains(ValidatorRegExp.PARAM_PATTERN));
			assertTrue(exception.getMessage().contains("format"));
		}
	}

}
