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
package fr.ybo.moteurcsv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.ybo.moteurcsv.annotation.CsvFile;
import fr.ybo.moteurcsv.annotation.CsvParam;
import org.junit.Test;

import fr.ybo.moteurcsv.adapter.AdapterDate;
import fr.ybo.moteurcsv.annotation.CsvColumn;
import fr.ybo.moteurcsv.exception.InvalidParamException;
import fr.ybo.moteurcsv.exception.MoteurCsvException;
import fr.ybo.moteurcsv.exception.CsvErrorsExceededException;
import fr.ybo.moteurcsv.modele.Erreur;
import fr.ybo.moteurcsv.modele.Resultat;

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
			new MoteurCsv(ObjetRg1.class);
			fail("Une excepion aurait du être levée");
		} catch (MoteurCsvException exception) {
			assertEquals(InvalidParamException.class, exception.getCause().getClass());
			assertTrue(exception.getCause().getMessage().contains("mandatory"));
			assertTrue(exception.getCause().getMessage().contains("format"));
		}
	}

	@Test
	public void testRg2() {
		try {
			new MoteurCsv(ObjetRg2.class);
			fail("Une excepion aurait du être levée");
		} catch (MoteurCsvException exception) {
			assertEquals(InvalidParamException.class, exception.getCause().getClass());
			assertTrue(exception.getCause().getMessage(), exception.getCause().getMessage().contains("tutu"));
			assertTrue(exception.getCause().getMessage(), exception.getCause().getMessage().contains("format"));
		}
	}

	private MoteurCsv moteurRg345 = new MoteurCsv(ObjetRg345.class);

	@Test
	public void testRg3() {
		InputStream stream = new StringStream("att,date\natt,tutu");
		try {
			moteurRg345.parseInputStream(stream, ObjetRg345.class);
			fail("Une exception aurait du être levée");
		} catch (CsvErrorsExceededException exception) {
			assertEquals(1, exception.getErrors().size());
			Erreur erreur = exception.getErrors().get(0);
			assertEquals("att,tutu", erreur.getLigneCsv());
			assertEquals(1, erreur.getMessages().size());
			assertTrue(erreur.getMessages().get(0), erreur.getMessages().get(0).contains("tutu"));
			assertTrue(erreur.getMessages().get(0), erreur.getMessages().get(0).contains("dd/MM/yyyy"));
		}
	}

	@Test
	public void testRg4() throws CsvErrorsExceededException {
		InputStream stream = new StringStream("att,date\natt,");
		Resultat<ObjetRg345> objets = moteurRg345.parseInputStream(stream, ObjetRg345.class);
		assertTrue(objets.getErreurs().isEmpty());
		assertEquals(1, objets.getObjets().size());
		ObjetRg345 objet = objets.getObjets().get(0);
		assertEquals("att", objet.att);
		assertNull(objet.date);
	}

	@Test
	public void testRg5() throws CsvErrorsExceededException {
		InputStream stream = new StringStream("att,date\natt,21/12/2012");
		Resultat<ObjetRg345> objets = moteurRg345.parseInputStream(stream, ObjetRg345.class);
		assertTrue(objets.getErreurs().isEmpty());
		assertEquals(1, objets.getObjets().size());
		ObjetRg345 objet = objets.getObjets().get(0);
		assertEquals("att", objet.att);
		assertEquals("21/12/2012", new SimpleDateFormat("dd/MM/yyyy").format(objet.date));
		StringWriter writer = new StringWriter();
		moteurRg345.writeFile(writer, objets.getObjets(), ObjetRg345.class);
		assertEquals("\"att\",\"date\"\n\"att\",\"21/12/2012\"\n", writer.getBuffer().toString());
	}

}
