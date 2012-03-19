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

import org.junit.Before;
import org.junit.Test;

import fr.ybo.moteurcsv.exception.InvalideParamException;

/**
 * Test de la classe {@link ValidatorTaille}.
 */
public class ValidatorTailleTest {

	private ValidatorTaille validator;
	private Map<String, String> params;

	@Before
	public void setup() {
		validator = new ValidatorTaille();
		params = new HashMap<String, String>();
	}

	@Test(expected = InvalideParamException.class)
	public void testSsParametre() throws InvalideParamException {
		validator.addParams(params);
	}

	@Test(expected = InvalideParamException.class)
	public void testTailleMinKo() throws InvalideParamException {
		params.put(ValidatorTaille.PARAM_TAILLE_MIN, "tutu");
		validator.addParams(params);
	}

	@Test(expected = InvalideParamException.class)
	public void testTailleMaxKo() throws InvalideParamException {
		params.put(ValidatorTaille.PARAM_TAILLE_MAX, "tutu");
		validator.addParams(params);
	}

	@Test
	public void testTailleMin() throws InvalideParamException, ValidateException {
		params.put(ValidatorTaille.PARAM_TAILLE_MIN, "2");
		validator.addParams(params);

		// OK
		validator.validate("doitEtreOk");
		validator.validate("Ok");

		// KO
		try {
			validator.validate("1");
			fail("Une exception aurait du être levée");
		} catch (ValidateException exception) {
			// TailleMin
			assertTrue(exception.getMessage().contains("2"));
			// Valeur.
			assertTrue(exception.getMessage().contains("1"));
		}
	}

	@Test
	public void testTailleMax() throws ValidateException, InvalideParamException {
		params.put(ValidatorTaille.PARAM_TAILLE_MAX, "5");
		validator.addParams(params);

		// OK
		validator.validate("Ok");
		validator.validate("OkPil");

		// KO
		try {
			validator.validate("DoitKo");
			fail("Une exception aurait du être levée");
		} catch (ValidateException exception) {
			// TailleMax
			assertTrue(exception.getMessage().contains("5"));
			// Valeur
			assertTrue(exception.getMessage().contains("DoitKo"));
		}
	}

	@Test
	public void testTailleMinEtTailleMax() throws ValidateException, InvalideParamException {
		params.put(ValidatorTaille.PARAM_TAILLE_MIN, "2");
		params.put(ValidatorTaille.PARAM_TAILLE_MAX, "5");
		validator.addParams(params);

		// OK
		validator.validate("Ok");
		validator.validate("OkPil");

		// KO
		try {
			validator.validate("1");
			fail("Une exception aurait du être levée");
		} catch (ValidateException exception) {
			// TailleMin
			assertTrue(exception.getMessage().contains("2"));
			// Valeur.
			assertTrue(exception.getMessage().contains("1"));
		}
		try {
			validator.validate("DoitKo");
			fail("Une exception aurait du être levée");
		} catch (ValidateException exception) {
			// TailleMax
			assertTrue(exception.getMessage().contains("5"));
			// Valeur
			assertTrue(exception.getMessage().contains("DoitKo"));
		}
	}

}
