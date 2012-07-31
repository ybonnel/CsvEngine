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
package fr.ybonnel.csvengine;

import fr.ybonnel.csvengine.annotation.CsvColumn;
import fr.ybonnel.csvengine.annotation.CsvFile;
import fr.ybonnel.csvengine.annotation.CsvValidation;
import fr.ybonnel.csvengine.exception.CsvErrorsExceededException;
import fr.ybonnel.csvengine.model.EngineParameters;
import fr.ybonnel.csvengine.model.Error;
import fr.ybonnel.csvengine.model.Result;
import fr.ybonnel.csvengine.validator.ValidatorEmail;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test of {@link fr.ybonnel.csvengine.validator.ValidatorEmail}.
 */
public class ValidatorEmailTest {

	@CsvFile
	public static class ObjetEmail {
		@CsvColumn("att")
		public String att;

		@CsvValidation(ValidatorEmail.class)
		@CsvColumn("email")
		public String email;
	}

	@Test
	public void testEmail() throws CsvErrorsExceededException {
		StringStream stream =
				new StringStream("att,email\n" + ",\n" + ",nonvalide@tutu\n" + ",nonvalide\n" + ",@nonvalide.fr\n"
						+ ",valide@valide.fr");
		CsvEngine engine =
				new CsvEngine(EngineParameters.createBuilder().setNbLinesWithErrorsToStop(999).build(), ObjetEmail.class);
		Result<ObjetEmail> result = engine.parseInputStream(stream, ObjetEmail.class);
		assertNotNull(result);
		assertEquals(2, result.getObjects().size());
		assertNull(result.getObjects().get(0).email);
		assertEquals("valide@valide.fr", result.getObjects().get(1).email);

		assertEquals(3, result.getErrors().size());
		fr.ybonnel.csvengine.model.Error error1 = result.getErrors().get(0);
		assertEquals(",nonvalide@tutu", error1.getCsvLine());
		assertEquals(1, error1.getMessages().size());
		assertTrue(error1.getMessages().get(0).contains("nonvalide@tutu"));

		Error error2 = result.getErrors().get(1);
		assertEquals(",nonvalide", error2.getCsvLine());
		assertEquals(1, error2.getMessages().size());
		assertTrue(error2.getMessages().get(0).contains("nonvalide"));

		Error error3 = result.getErrors().get(2);
		assertEquals(",@nonvalide.fr", error3.getCsvLine());
		assertEquals(1, error3.getMessages().size());
		assertTrue(error3.getMessages().get(0).contains("@nonvalide.fr"));
	}

}
