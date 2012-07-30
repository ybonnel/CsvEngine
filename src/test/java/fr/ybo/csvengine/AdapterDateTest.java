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
package fr.ybo.csvengine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.ybo.csvengine.annotation.CsvFile;
import fr.ybo.csvengine.annotation.CsvParam;
import org.junit.Test;

import fr.ybo.csvengine.adapter.AdapterDate;
import fr.ybo.csvengine.annotation.CsvColumn;
import fr.ybo.csvengine.exception.InvalidParamException;
import fr.ybo.csvengine.exception.CsvEngineException;
import fr.ybo.csvengine.exception.CsvErrorsExceededException;
import fr.ybo.csvengine.model.Error;
import fr.ybo.csvengine.model.Result;

/**
 * Test de l'adapter de date.<br/>
 * Règles de gestion :
 * <ul>
 * <li>RG1 : pattern non fourni.</li>
 * <li>RG2 : pattern invalide.</li>
 * <li>RG3 : format de date invalide</li>
 * <li>RG4 : date null</li>
 * <li>RG5 : date valide</li>
 * </ul>
 */
public class AdapterDateTest {

	@CsvFile
	public static class ObjetRg1 {

		@CsvColumn(value = "date", adapter = AdapterDate.class)
		public Date date;
	}

	@CsvFile
	public static class ObjetRg2 {

		@CsvColumn(value = "date", adapter = AdapterDate.class,
				params = { @CsvParam(name = AdapterDate.PARAM_FORMAT, value = "tutu") })
		public Date date;
	}

	@CsvFile
	public static class ObjetRg345 {

		@CsvColumn("att")
		public String att;

		@CsvColumn(value = "date", adapter = AdapterDate.class,
				params = { @CsvParam(name = AdapterDate.PARAM_FORMAT, value = "dd/MM/yyyy") })
		public Date date;
	}

	@Test
	public void testRg1() {
		try {
			new CsvEngine(ObjetRg1.class);
			fail("Une excepion aurait du être levée");
		} catch (CsvEngineException exception) {
			assertEquals(InvalidParamException.class, exception.getCause().getClass());
			assertTrue(exception.getCause().getMessage().contains("mandatory"));
			assertTrue(exception.getCause().getMessage().contains("format"));
		}
	}

	@Test
	public void testRg2() {
		try {
			new CsvEngine(ObjetRg2.class);
			fail("Une excepion aurait du être levée");
		} catch (CsvEngineException exception) {
			assertEquals(InvalidParamException.class, exception.getCause().getClass());
			assertTrue(exception.getCause().getMessage(), exception.getCause().getMessage().contains("tutu"));
			assertTrue(exception.getCause().getMessage(), exception.getCause().getMessage().contains("format"));
		}
	}

	private CsvEngine rg345Engine = new CsvEngine(ObjetRg345.class);

	@Test
	public void testRg3() {
		InputStream stream = new StringStream("att,date\natt,tutu");
		try {
			rg345Engine.parseInputStream(stream, ObjetRg345.class);
			fail("Une exception aurait du être levée");
		} catch (CsvErrorsExceededException exception) {
			assertEquals(1, exception.getErrors().size());
			Error error = exception.getErrors().get(0);
			assertEquals("att,tutu", error.getCsvLine());
			assertEquals(1, error.getMessages().size());
			assertTrue(error.getMessages().get(0), error.getMessages().get(0).contains("tutu"));
			assertTrue(error.getMessages().get(0), error.getMessages().get(0).contains("dd/MM/yyyy"));
		}
	}

	@Test
	public void testRg4() throws CsvErrorsExceededException {
		InputStream stream = new StringStream("att,date\natt,");
		Result<ObjetRg345> objets = rg345Engine.parseInputStream(stream, ObjetRg345.class);
		assertTrue(objets.getErrors().isEmpty());
		assertEquals(1, objets.getObjects().size());
		ObjetRg345 objet = objets.getObjects().get(0);
		assertEquals("att", objet.att);
		assertNull(objet.date);
	}

	@Test
	public void testRg5() throws CsvErrorsExceededException {
		InputStream stream = new StringStream("att,date\natt,21/12/2012");
		Result<ObjetRg345> objets = rg345Engine.parseInputStream(stream, ObjetRg345.class);
		assertTrue(objets.getErrors().isEmpty());
		assertEquals(1, objets.getObjects().size());
		ObjetRg345 objet = objets.getObjects().get(0);
		assertEquals("att", objet.att);
		assertEquals("21/12/2012", new SimpleDateFormat("dd/MM/yyyy").format(objet.date));
		StringWriter writer = new StringWriter();
		rg345Engine.writeFile(writer, objets.getObjects(), ObjetRg345.class);
		assertEquals("\"att\",\"date\"\n\"att\",\"21/12/2012\"\n", writer.getBuffer().toString());
	}

}
