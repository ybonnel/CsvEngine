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
import fr.ybonnel.csvengine.exception.CsvEngineException;
import fr.ybonnel.csvengine.exception.CsvErrorsExceededException;
import fr.ybonnel.csvengine.model.EngineParameters;
import fr.ybonnel.csvengine.model.Error;
import fr.ybonnel.csvengine.model.Result;
import fr.ybonnel.csvengine.validator.ValidateException;
import fr.ybonnel.csvengine.validator.ValidatorCsv;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Validation test.
 * <ul>
 * <li>RULE1 : validate a field on mandatory side.</li>
 * <li>RULE2 : validate a field with a specific class.</li>
 * <li>RULE3 : can desactivate validation.</li>
 * <li>RULE4 : can specify the max number of errors.</li>
 * </ul>
 * 
 * @author ybonnel
 * 
 */
public class ValidationTest {

	/**
	 * Class for Rule1.
	 */
	@CsvFile()
	public static class ObjectRule1_1 {
		/**
		 * Optional.
		 */
		@CsvColumn(value = "att1")
		public String att1;
		/**
		 * Optional.
		 */
		@CsvColumn(value = "att2")
		public String att2;
	}

	/**
	 * Class for Rule1.
	 */
	@CsvFile()
	public static class ObjectRule1_2 {
		/**
		 * Mandatory.
		 */
		@CsvColumn(value = "att1", mandatory = true)
		public String att1;
		/**
		 * Optional.
		 */
		@CsvColumn(value = "att2")
		public String att2;
	}

	/**
	 * Class for Rule1.
	 */
	@CsvFile()
	public static class ObjectRule1_3 {
		/**
		 * Optional.
		 */
		@CsvColumn(value = "att1")
		public String att1;
		/**
		 * Mandatory.
		 */
		@CsvColumn(value = "att2", mandatory = true)
		public String att2;
	}

	/**
	 * Class for Rule1.
	 */
	@CsvFile()
	public static class ObjectRule1_4 {
		/**
		 * Mandatory.
		 */
		@CsvColumn(value = "att1", mandatory = true)
		public String att1;
		/**
		 * Mandatory.
		 */
		@CsvColumn(value = "att2", mandatory = true)
		public String att2;
	}

	/**
	 * Construction of streams and engine useful for tests.
	 */
	@Before
	public void setup() {
		streamRule1 = new StringStream("att1,att2\nval1,\n,val2\n,");
		rule1Engine =
				new CsvEngine(EngineParameters.createBuilder().setValidation(true).setNbLinesWithErrorsToStop(999).build(),
						ObjectRule1_1.class, ObjectRule1_2.class, ObjectRule1_3.class, ObjectRule1_4.class);

	}

	/**
	 * Stream to test Rule1.
	 */
	private InputStream streamRule1;
	/**
	 * Engine to test Rule1.
	 */
	private CsvEngine rule1Engine;

	/**
	 * Test of Rule1 with two optional fields.
	 * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException exception.
	 */
	@Test
	public void testRule1_1() throws CsvErrorsExceededException {
		// Test with two optional fields.
		Result<ObjectRule1_1> result = rule1Engine.parseInputStream(streamRule1, ObjectRule1_1.class);
		assertNotNull(result);
		assertEquals(3, result.getObjects().size());
		assertEquals(0, result.getErrors().size());
	}

	/**
	 * Test of Rule1 with first field mandatory and second
	 * optional.
	 * 
	 * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRule1_2() throws CsvErrorsExceededException {
		// Test with first field mandatory.
		Result<ObjectRule1_2> result = rule1Engine.parseInputStream(streamRule1, ObjectRule1_2.class);
		assertNotNull(result);
		assertEquals(1, result.getObjects().size());
		assertEquals(2, result.getErrors().size());
		fr.ybonnel.csvengine.model.Error error1 = result.getErrors().get(0);
		assertEquals(",val2", error1.getCsvLine());
		assertEquals(1, error1.getMessages().size());
		assertTrue(error1.getMessages().get(0).contains("mandatory"));
		assertTrue(error1.getMessages().get(0).contains("att1"));
		Error error2 = result.getErrors().get(1);
		assertEquals(",", error2.getCsvLine());
		assertEquals(1, error2.getMessages().size());
		assertTrue(error2.getMessages().get(0).contains("mandatory"));
		assertTrue(error2.getMessages().get(0).contains("att1"));
	}

	/**
	 * Test of Rule1 with first field optional and second
	 * mandatory.
	 * 
	 * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRule1_3() throws CsvErrorsExceededException {
		// Test with second field mandatory.
		Result<ObjectRule1_3> result = rule1Engine.parseInputStream(streamRule1, ObjectRule1_3.class);
		assertNotNull(result);
		assertEquals(1, result.getObjects().size());
		assertEquals(2, result.getErrors().size());
		Error error1 = result.getErrors().get(0);
		assertEquals("val1,", error1.getCsvLine());
		assertEquals(1, error1.getMessages().size());
		assertTrue(error1.getMessages().get(0).contains("mandatory"));
		assertTrue(error1.getMessages().get(0).contains("att2"));
		Error error2 = result.getErrors().get(1);
		assertEquals(",", error2.getCsvLine());
		assertEquals(1, error2.getMessages().size());
		assertTrue(error2.getMessages().get(0).contains("mandatory"));
		assertTrue(error2.getMessages().get(0).contains("att2"));
	}

	/**
	 * Test of Rule1 with two fields mandatory.
	 * 
	 * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRule1_4() throws CsvErrorsExceededException {
		// Test with two fields mandatory.
		Result<ObjectRule1_4> result = rule1Engine.parseInputStream(streamRule1, ObjectRule1_4.class);
		assertNotNull(result);
		assertEquals(0, result.getObjects().size());
		assertEquals(3, result.getErrors().size());
		Error error1 = result.getErrors().get(0);
		assertEquals("val1,", error1.getCsvLine());
		assertEquals(1, error1.getMessages().size());
		assertTrue(error1.getMessages().get(0).contains("mandatory"));
		assertTrue(error1.getMessages().get(0).contains("att2"));
		Error error2 = result.getErrors().get(1);
		assertEquals(",val2", error2.getCsvLine());
		assertEquals(1, error2.getMessages().size());
		assertTrue(error2.getMessages().get(0).contains("mandatory"));
		assertTrue(error2.getMessages().get(0).contains("att1"));
		Error error3 = result.getErrors().get(2);
		assertEquals(",", error3.getCsvLine());
		assertEquals(2, error3.getMessages().size());
		assertTrue(error3.getMessages().get(0).contains("mandatory"));
		assertTrue(error3.getMessages().get(0).contains("att1"));
		assertTrue(error3.getMessages().get(1).contains("mandatory"));
		assertTrue(error3.getMessages().get(1).contains("att2"));
	}

	@CsvFile
	public static class ObjectRule2 {
		@CsvValidation(ValidationRule2.class)
		@CsvColumn("att1")
		public String att1;
		@CsvValidation(ValidationRule2.class)
		@CsvColumn("att2")
		public String att2;
	}

	public static class ValidationRule2 extends ValidatorCsv {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ValidatorCsv#validate(java.lang.String)
		 */
		public void validate(String field) throws ValidateException {
			if (!"Rule2".equals(field)) {
				throw new ValidateException("The field must be equals to Rule2");
			}
		}

	}

	/**
	 * Test de la Rule2.
	 * 
	 * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRule2() throws CsvErrorsExceededException {
		CsvEngine Rule2Engine =
				new CsvEngine(EngineParameters.createBuilder().setValidation(true).setNbLinesWithErrorsToStop(999).build(),
						ObjectRule2.class);
		InputStream stream = new StringStream("att1,att2\nRule2,Rule2\nRule2,Rule1\nRule1,Rule2\nRule1,Rule3");

		Result<ObjectRule2> result = Rule2Engine.parseInputStream(stream, ObjectRule2.class);
		assertNotNull(result);
		assertEquals(1, result.getObjects().size());
		assertEquals(3, result.getErrors().size());
		Error error1 = result.getErrors().get(0);
		assertEquals("Rule2,Rule1", error1.getCsvLine());
		assertEquals(1, error1.getMessages().size());
		assertTrue(error1.getMessages().get(0).endsWith("The field must be equals to Rule2"));
		assertTrue(error1.getMessages().get(0).contains("att2"));
		Error error2 = result.getErrors().get(1);
		assertEquals("Rule1,Rule2", error2.getCsvLine());
		assertEquals(1, error2.getMessages().size());
		assertTrue(error2.getMessages().get(0).endsWith("The field must be equals to Rule2"));
		assertTrue(error2.getMessages().get(0).contains("att1"));
		Error error3 = result.getErrors().get(2);
		assertEquals("Rule1,Rule3", error3.getCsvLine());
		assertEquals(2, error3.getMessages().size());
		assertTrue(error3.getMessages().get(0).endsWith("The field must be equals to Rule2"));
		assertTrue(error3.getMessages().get(0).contains("att1"));
		assertTrue(error3.getMessages().get(1).endsWith("The field must be equals to Rule2"));
		assertTrue(error3.getMessages().get(1).contains("att2"));
	}

	/**
	 * Test of Rule3.<br/>
	 * Test with data of Rule1_4 (mandatory fields).
	 * 
	 * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRule3_1() throws CsvErrorsExceededException {
		rule1Engine.getParameters().setValidation(false);
		Result<ObjectRule1_4> result = rule1Engine.parseInputStream(streamRule1, ObjectRule1_4.class);
		assertNotNull(result);
		assertEquals(3, result.getObjects().size());
	}

	/**
	 * Test of Rule3.<br/>
	 * Test with data of Rule2 (with validator).
	 * 
	 * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRule3_2() throws CsvErrorsExceededException {
		CsvEngine Rule2Engine =
				new CsvEngine(EngineParameters.createBuilder().setValidation(false).setNbLinesWithErrorsToStop(999).build(),
						ObjectRule2.class);
		InputStream stream = new StringStream("att1,att2\nRule2,Rule2\nRule2,Rule1\nRule1,Rule2\nRule1,Rule3");

		Result<ObjectRule2> result = Rule2Engine.parseInputStream(stream, ObjectRule2.class);
		assertNotNull(result);
		assertEquals(4, result.getObjects().size());
	}

	/**
	 * Test of Rule4. Test with max errors at 1.
	 */
	@Test
	public void testRule4_1() {
		CsvEngine Rule2Engine =
				new CsvEngine(EngineParameters.createBuilder().setValidation(true).setNbLinesWithErrorsToStop(1).build(),
						ObjectRule2.class);
		InputStream stream = new StringStream("att1,att2\nRule2,Rule2\nRule2,Rule1\nRule1,Rule2\nRule1,Rule3");

		try {
			Rule2Engine.parseInputStream(stream, ObjectRule2.class);
            fail("An exception must be throw");
		} catch (CsvErrorsExceededException exception) {
			assertEquals(2, exception.getErrors().size());
			assertNotNull(exception.getMessage());
		}
	}

	/**
	 * Test de la Rule4. Test with max errors at 0.
	 */
	@Test
	public void testRule4_2() {
		CsvEngine Rule2Engine =
				new CsvEngine(EngineParameters.createBuilder().setValidation(true).setNbLinesWithErrorsToStop(0).build(),
						ObjectRule2.class);
		InputStream stream = new StringStream("att1,att2\nRule2,Rule2\nRule2,Rule1\nRule1,Rule2\nRule1,Rule3");

		try {
			Rule2Engine.parseInputStream(stream, ObjectRule2.class);
            fail("An exception must be throw");
		} catch (CsvErrorsExceededException exception) {
			assertEquals(1, exception.getErrors().size());
		}
	}

	/**
	 * Test de la Rule4. Test with max errors at -1
	 * (infinite).
	 * 
	 * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRule4_3() throws CsvErrorsExceededException {
		CsvEngine Rule2Engine =
				new CsvEngine(EngineParameters.createBuilder().setValidation(true).setNbLinesWithErrorsToStop(-1).build(),
						ObjectRule2.class);
		InputStream stream = new StringStream("att1,att2\nRule2,Rule2\nRule2,Rule1\nRule1,Rule2\nRule1,Rule3");

		Result<ObjectRule2> result = Rule2Engine.parseInputStream(stream, ObjectRule2.class);
		assertNotNull(result);
		assertEquals(1, result.getObjects().size());
		assertEquals(3, result.getErrors().size());
	}

	/**
	 * Test de la Rule4. Test with max errors at 3.
	 * 
	 * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRule4_4() throws CsvErrorsExceededException {
		CsvEngine Rule2Engine =
				new CsvEngine(EngineParameters.createBuilder().setValidation(true).setNbLinesWithErrorsToStop(3).build(),
						ObjectRule2.class);
		InputStream stream = new StringStream("att1,att2\nRule2,Rule2\nRule2,Rule1\nRule1,Rule2\nRule1,Rule3");

		Result<ObjectRule2> result = Rule2Engine.parseInputStream(stream, ObjectRule2.class);
		assertNotNull(result);
		assertEquals(1, result.getObjects().size());
		assertEquals(3, result.getErrors().size());
	}

	public static class ValidatorWithErrorDuringCreation extends ValidatorCsv {

		public ValidatorWithErrorDuringCreation(String something) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ValidatorCsv#validate(java.lang.String)
		 */
		@Override
		public void validate(String field) throws ValidateException {
		}

	}

	@CsvFile
	public static class ObjectWithErrorDuringValidatorCreation {
		@CsvValidation(ValidatorWithErrorDuringCreation.class)
		@CsvColumn("att")
		public String att;
	}

	@Test
	public void testErrorDuringValidatorCreation() {
		try {
			new CsvEngine(ObjectWithErrorDuringValidatorCreation.class);
		} catch (CsvEngineException exception) {
			assertTrue(exception.getMessage().contains(ValidatorWithErrorDuringCreation.class.getSimpleName()));
		}
	}

}
