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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.ybonnel.csvengine.annotation.CsvFile;
import fr.ybonnel.csvengine.annotation.CsvParam;
import fr.ybonnel.csvengine.adapter.AdapterDate;
import fr.ybonnel.csvengine.annotation.CsvColumn;
import fr.ybonnel.csvengine.exception.CsvEngineException;
import fr.ybonnel.csvengine.exception.CsvErrorsExceededException;
import fr.ybonnel.csvengine.exception.InvalidParamException;
import org.junit.Test;

import fr.ybonnel.csvengine.model.Result;

/**
 * Test of date Adapter.<br/>
 * Rule :
 * <ul>
 * <li>Rule1 : no pattern.</li>
 * <li>Rule2 : invalid pattern.</li>
 * <li>Rule3 : invalid date format</li>
 * <li>Rule4 : null date</li>
 * <li>Rule5 : valid date</li>
 * </ul>
 */
public class AdapterDateTest {

	@CsvFile
	public static class ObjetRule1 {

		@CsvColumn(value = "date", adapter = AdapterDate.class)
		public Date date;
	}

	@CsvFile
	public static class ObjetRule2 {

		@CsvColumn(value = "date", adapter = AdapterDate.class,
				params = { @CsvParam(name = AdapterDate.PARAM_FORMAT, value = "tutu") })
		public Date date;
	}

	@CsvFile
	public static class ObjetRule345 {

		@CsvColumn("att")
		public String att;

		@CsvColumn(value = "date", adapter = AdapterDate.class,
				params = { @CsvParam(name = AdapterDate.PARAM_FORMAT, value = "dd/MM/yyyy") })
		public Date date;
	}

	@Test
	public void testRule1() {
		try {
			new CsvEngine(ObjetRule1.class);
            fail("An exception must be throw");
		} catch (CsvEngineException exception) {
			assertEquals(InvalidParamException.class, exception.getCause().getClass());
			assertTrue(exception.getCause().getMessage().contains("mandatory"));
			assertTrue(exception.getCause().getMessage().contains("format"));
		}
	}

	@Test
	public void testRule2() {
		try {
			new CsvEngine(ObjetRule2.class);
            fail("An exception must be throw");
		} catch (CsvEngineException exception) {
			assertEquals(InvalidParamException.class, exception.getCause().getClass());
			assertTrue(exception.getCause().getMessage(), exception.getCause().getMessage().contains("tutu"));
			assertTrue(exception.getCause().getMessage(), exception.getCause().getMessage().contains("format"));
		}
	}

	private CsvEngine rule345Engine = new CsvEngine(ObjetRule345.class);

	@Test
	public void testRule3() {
		InputStream stream = new StringStream("att,date\natt,tutu");
		try {
			rule345Engine.parseInputStream(stream, ObjetRule345.class);
            fail("An exception must be throw");
		} catch (CsvErrorsExceededException exception) {
			assertEquals(1, exception.getErrors().size());
			fr.ybonnel.csvengine.model.Error error = exception.getErrors().get(0);
			assertEquals("att,tutu", error.getCsvLine());
			assertEquals(1, error.getMessages().size());
			assertTrue(error.getMessages().get(0), error.getMessages().get(0).contains("tutu"));
			assertTrue(error.getMessages().get(0), error.getMessages().get(0).contains("dd/MM/yyyy"));
		}
	}

	@Test
	public void testRule4() throws CsvErrorsExceededException {
		InputStream stream = new StringStream("att,date\natt,");
		Result<ObjetRule345> objets = rule345Engine.parseInputStream(stream, ObjetRule345.class);
		assertTrue(objets.getErrors().isEmpty());
		assertEquals(1, objets.getObjects().size());
		ObjetRule345 objet = objets.getObjects().get(0);
		assertEquals("att", objet.att);
		assertNull(objet.date);
	}

	@Test
	public void testRule5() throws CsvErrorsExceededException {
		InputStream stream = new StringStream("att,date\natt,21/12/2012");
		Result<ObjetRule345> objets = rule345Engine.parseInputStream(stream, ObjetRule345.class);
		assertTrue(objets.getErrors().isEmpty());
		assertEquals(1, objets.getObjects().size());
		ObjetRule345 objet = objets.getObjects().get(0);
		assertEquals("att", objet.att);
		assertEquals("21/12/2012", new SimpleDateFormat("dd/MM/yyyy").format(objet.date));
		StringWriter writer = new StringWriter();
        rule345Engine.writeFile(writer, objets.getObjects(), ObjetRule345.class);
		assertEquals("\"att\",\"date\"\n\"att\",\"21/12/2012\"\n", writer.getBuffer().toString());
	}

}
