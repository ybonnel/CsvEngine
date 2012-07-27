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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;

import fr.ybo.moteurcsv.annotation.CsvColumn;
import fr.ybo.moteurcsv.annotation.CsvFile;
import fr.ybo.moteurcsv.annotation.CsvValidation;
import org.junit.Before;
import org.junit.Test;

import fr.ybo.moteurcsv.exception.MoteurCsvException;
import fr.ybo.moteurcsv.exception.CsvErrorsExceededException;
import fr.ybo.moteurcsv.modele.Erreur;
import fr.ybo.moteurcsv.modele.ParametresMoteur;
import fr.ybo.moteurcsv.modele.Resultat;
import fr.ybo.moteurcsv.validator.ValidateException;
import fr.ybo.moteurcsv.validator.ValidatorCsv;

/**
 * Tester la validation.
 * <ul>
 * <li>REGLE RG1 : permettre de valider un champ sur son côté mandatory.</li>
 * <li>REGLE RG2 : permettre de réaliser n'importe qu'elle validation via des
 * classes spécifiques.</li>
 * <li>REGLE RG3 : permettre de désactiver la validation.</li>
 * <li>REGLE RG4 : permettre d'accepter un nombre acceptable d'erreur.</li>
 * </ul>
 * 
 * @author ybonnel
 * 
 */
public class ValidationTest {

	/**
	 * Classe pour la RG1.
	 */
	@CsvFile()
	public static class ObjetRg1_1 {
		/**
		 * Facultatif.
		 */
		@CsvColumn(value = "att1")
		public String att1;
		/**
		 * Facultatif.
		 */
		@CsvColumn(value = "att2")
		public String att2;
	}

	/**
	 * Classe pour la RG1.
	 */
	@CsvFile()
	public static class ObjetRg1_2 {
		/**
		 * Obligatoire.
		 */
		@CsvColumn(value = "att1", mandatory = true)
		public String att1;
		/**
		 * Facultatif.
		 */
		@CsvColumn(value = "att2")
		public String att2;
	}

	/**
	 * Classe pour la RG1.
	 */
	@CsvFile()
	public static class ObjetRg1_3 {
		/**
		 * Facultatif.
		 */
		@CsvColumn(value = "att1")
		public String att1;
		/**
		 * Obligatoire.
		 */
		@CsvColumn(value = "att2", mandatory = true)
		public String att2;
	}

	/**
	 * Classe pour la RG1.
	 */
	@CsvFile()
	public static class ObjetRg1_4 {
		/**
		 * Obligatoire.
		 */
		@CsvColumn(value = "att1", mandatory = true)
		public String att1;
		/**
		 * Obligatoire.
		 */
		@CsvColumn(value = "att2", mandatory = true)
		public String att2;
	}

	/**
	 * Construction des streams et des moteurs nécessaires aux tests.
	 */
	@Before
	public void setup() {
		streamRg1 = new StringStream("att1,att2\nval1,\n,val2\n,");
		moteurRg1 =
				new MoteurCsv(ParametresMoteur.createBuilder().setValidation(true).setNbLinesWithErrorsToStop(999).build(),
						ObjetRg1_1.class, ObjetRg1_2.class, ObjetRg1_3.class, ObjetRg1_4.class);

	}

	/**
	 * Stream pour tester la RG1.
	 */
	private InputStream streamRg1;
	/**
	 * Moteur pour tester la RG1.
	 */
	private MoteurCsv moteurRg1;

	/**
	 * Test de la RG1 avec les deux champs facultatifs.
	 * @throws fr.ybo.moteurcsv.exception.CsvErrorsExceededException exception.
	 */
	@Test
	public void testRg1_1() throws CsvErrorsExceededException {
		// Test avec les deux champs facultatif.
		Resultat<ObjetRg1_1> resultat = moteurRg1.parseInputStream(streamRg1, ObjetRg1_1.class);
		assertNotNull(resultat);
		assertEquals(3, resultat.getObjets().size());
		assertEquals(0, resultat.getErreurs().size());
	}

	/**
	 * Test de la RG1 avec le premiers champ mandatory et le second
	 * facultatif.
	 * 
	 * @throws fr.ybo.moteurcsv.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRg1_2() throws CsvErrorsExceededException {
		// Test avec le premier champ mandatory.
		Resultat<ObjetRg1_2> resultat = moteurRg1.parseInputStream(streamRg1, ObjetRg1_2.class);
		assertNotNull(resultat);
		assertEquals(1, resultat.getObjets().size());
		assertEquals(2, resultat.getErreurs().size());
		Erreur erreur1 = resultat.getErreurs().get(0);
		assertEquals(",val2", erreur1.getLigneCsv());
		assertEquals(1, erreur1.getMessages().size());
		assertTrue(erreur1.getMessages().get(0).contains("mandatory"));
		assertTrue(erreur1.getMessages().get(0).contains("att1"));
		Erreur erreur2 = resultat.getErreurs().get(1);
		assertEquals(",", erreur2.getLigneCsv());
		assertEquals(1, erreur2.getMessages().size());
		assertTrue(erreur2.getMessages().get(0).contains("mandatory"));
		assertTrue(erreur2.getMessages().get(0).contains("att1"));
	}

	/**
	 * Test de la RG1 avec le premiers champ facultatif et le second
	 * mandatory.
	 * 
	 * @throws fr.ybo.moteurcsv.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRg1_3() throws CsvErrorsExceededException {
		// Test avec le deuxième champ mandatory.
		Resultat<ObjetRg1_3> resultat = moteurRg1.parseInputStream(streamRg1, ObjetRg1_3.class);
		assertNotNull(resultat);
		assertEquals(1, resultat.getObjets().size());
		assertEquals(2, resultat.getErreurs().size());
		Erreur erreur1 = resultat.getErreurs().get(0);
		assertEquals("val1,", erreur1.getLigneCsv());
		assertEquals(1, erreur1.getMessages().size());
		assertTrue(erreur1.getMessages().get(0).contains("mandatory"));
		assertTrue(erreur1.getMessages().get(0).contains("att2"));
		Erreur erreur2 = resultat.getErreurs().get(1);
		assertEquals(",", erreur2.getLigneCsv());
		assertEquals(1, erreur2.getMessages().size());
		assertTrue(erreur2.getMessages().get(0).contains("mandatory"));
		assertTrue(erreur2.getMessages().get(0).contains("att2"));
	}

	/**
	 * Test de la RG1 avec les deux champs mandatory.
	 * 
	 * @throws fr.ybo.moteurcsv.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRg1_4() throws CsvErrorsExceededException {
		// Test avec les deux champs obligatoires
		Resultat<ObjetRg1_4> resultat = moteurRg1.parseInputStream(streamRg1, ObjetRg1_4.class);
		assertNotNull(resultat);
		assertEquals(0, resultat.getObjets().size());
		assertEquals(3, resultat.getErreurs().size());
		Erreur erreur1 = resultat.getErreurs().get(0);
		assertEquals("val1,", erreur1.getLigneCsv());
		assertEquals(1, erreur1.getMessages().size());
		assertTrue(erreur1.getMessages().get(0).contains("mandatory"));
		assertTrue(erreur1.getMessages().get(0).contains("att2"));
		Erreur erreur2 = resultat.getErreurs().get(1);
		assertEquals(",val2", erreur2.getLigneCsv());
		assertEquals(1, erreur2.getMessages().size());
		assertTrue(erreur2.getMessages().get(0).contains("mandatory"));
		assertTrue(erreur2.getMessages().get(0).contains("att1"));
		Erreur erreur3 = resultat.getErreurs().get(2);
		assertEquals(",", erreur3.getLigneCsv());
		assertEquals(2, erreur3.getMessages().size());
		assertTrue(erreur3.getMessages().get(0).contains("mandatory"));
		assertTrue(erreur3.getMessages().get(0).contains("att1"));
		assertTrue(erreur3.getMessages().get(1).contains("mandatory"));
		assertTrue(erreur3.getMessages().get(1).contains("att2"));
	}

	@CsvFile
	public static class ObjetRg2 {
		@CsvValidation(ValidationRg2.class)
		@CsvColumn("att1")
		public String att1;
		@CsvValidation(ValidationRg2.class)
		@CsvColumn("att2")
		public String att2;
	}

	public static class ValidationRg2 extends ValidatorCsv {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * fr.ybo.moteurcsv.validator.ValidatorCsv#validate(java.lang.String)
		 */
		public void validate(String champ) throws ValidateException {
			if (!"RG2".equals(champ)) {
				throw new ValidateException("Le champs doit être également à RG2");
			}
		}

	}

	/**
	 * Test de la RG2.
	 * 
	 * @throws fr.ybo.moteurcsv.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRg2() throws CsvErrorsExceededException {
		MoteurCsv moteurRg2 =
				new MoteurCsv(ParametresMoteur.createBuilder().setValidation(true).setNbLinesWithErrorsToStop(999).build(),
						ObjetRg2.class);
		InputStream stream = new StringStream("att1,att2\nRG2,RG2\nRG2,RG1\nRG1,RG2\nRG1,RG3");

		Resultat<ObjetRg2> resultat = moteurRg2.parseInputStream(stream, ObjetRg2.class);
		assertNotNull(resultat);
		assertEquals(1, resultat.getObjets().size());
		assertEquals(3, resultat.getErreurs().size());
		Erreur erreur1 = resultat.getErreurs().get(0);
		assertEquals("RG2,RG1", erreur1.getLigneCsv());
		assertEquals(1, erreur1.getMessages().size());
		assertTrue(erreur1.getMessages().get(0).endsWith("Le champs doit être également à RG2"));
		assertTrue(erreur1.getMessages().get(0).contains("att2"));
		Erreur erreur2 = resultat.getErreurs().get(1);
		assertEquals("RG1,RG2", erreur2.getLigneCsv());
		assertEquals(1, erreur2.getMessages().size());
		assertTrue(erreur2.getMessages().get(0).endsWith("Le champs doit être également à RG2"));
		assertTrue(erreur2.getMessages().get(0).contains("att1"));
		Erreur erreur3 = resultat.getErreurs().get(2);
		assertEquals("RG1,RG3", erreur3.getLigneCsv());
		assertEquals(2, erreur3.getMessages().size());
		assertTrue(erreur3.getMessages().get(0).endsWith("Le champs doit être également à RG2"));
		assertTrue(erreur3.getMessages().get(0).contains("att1"));
		assertTrue(erreur3.getMessages().get(1).endsWith("Le champs doit être également à RG2"));
		assertTrue(erreur3.getMessages().get(1).contains("att2"));
	}

	/**
	 * Test de la RG3.<br/>
	 * Test avec le jeu de données de RG1_4 (attributs obligatoires).
	 * 
	 * @throws fr.ybo.moteurcsv.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRg3_1() throws CsvErrorsExceededException {
		moteurRg1.getParametres().setValidation(false);
		Resultat<ObjetRg1_4> resultat = moteurRg1.parseInputStream(streamRg1, ObjetRg1_4.class);
		assertNotNull(resultat);
		assertEquals(3, resultat.getObjets().size());
	}

	/**
	 * Test de la RG3.<br/>
	 * Test avec le jeu de données de RG2 (avec validateur).
	 * 
	 * @throws fr.ybo.moteurcsv.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRg3_2() throws CsvErrorsExceededException {
		MoteurCsv moteurRg2 =
				new MoteurCsv(ParametresMoteur.createBuilder().setValidation(false).setNbLinesWithErrorsToStop(999).build(),
						ObjetRg2.class);
		InputStream stream = new StringStream("att1,att2\nRG2,RG2\nRG2,RG1\nRG1,RG2\nRG1,RG3");

		Resultat<ObjetRg2> resultat = moteurRg2.parseInputStream(stream, ObjetRg2.class);
		assertNotNull(resultat);
		assertEquals(4, resultat.getObjets().size());
	}

	/**
	 * Test de la RG4. Test en mettant le nombre d'erreurs acceptées à 1.
	 */
	@Test
	public void testRg4_1() {
		MoteurCsv moteurRg2 =
				new MoteurCsv(ParametresMoteur.createBuilder().setValidation(true).setNbLinesWithErrorsToStop(1).build(),
						ObjetRg2.class);
		InputStream stream = new StringStream("att1,att2\nRG2,RG2\nRG2,RG1\nRG1,RG2\nRG1,RG3");

		try {
			moteurRg2.parseInputStream(stream, ObjetRg2.class);
			fail("Une exception devrait être levée.");
		} catch (CsvErrorsExceededException exception) {
			assertEquals(2, exception.getErrors().size());
			assertNotNull(exception.getMessage());
		}
	}

	/**
	 * Test de la RG4. Test en mettant le nombre d'erreurs acceptées à 0.
	 */
	@Test
	public void testRg4_2() {
		MoteurCsv moteurRg2 =
				new MoteurCsv(ParametresMoteur.createBuilder().setValidation(true).setNbLinesWithErrorsToStop(0).build(),
						ObjetRg2.class);
		InputStream stream = new StringStream("att1,att2\nRG2,RG2\nRG2,RG1\nRG1,RG2\nRG1,RG3");

		try {
			moteurRg2.parseInputStream(stream, ObjetRg2.class);
			fail("Une exception devrait être levée.");
		} catch (CsvErrorsExceededException exception) {
			assertEquals(1, exception.getErrors().size());
		}
	}

	/**
	 * Test de la RG4. Test en mettant le nombre d'erreurs acceptées à -1
	 * (infinie).
	 * 
	 * @throws fr.ybo.moteurcsv.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRg4_3() throws CsvErrorsExceededException {
		MoteurCsv moteurRg2 =
				new MoteurCsv(ParametresMoteur.createBuilder().setValidation(true).setNbLinesWithErrorsToStop(-1).build(),
						ObjetRg2.class);
		InputStream stream = new StringStream("att1,att2\nRG2,RG2\nRG2,RG1\nRG1,RG2\nRG1,RG3");

		Resultat<ObjetRg2> resultat = moteurRg2.parseInputStream(stream, ObjetRg2.class);
		assertNotNull(resultat);
		assertEquals(1, resultat.getObjets().size());
		assertEquals(3, resultat.getErreurs().size());
	}

	/**
	 * Test de la RG4. Test en mettant le nombre d'erreurs acceptées à 3.
	 * 
	 * @throws fr.ybo.moteurcsv.exception.CsvErrorsExceededException
	 *             exception.
	 */
	@Test
	public void testRg4_4() throws CsvErrorsExceededException {
		MoteurCsv moteurRg2 =
				new MoteurCsv(ParametresMoteur.createBuilder().setValidation(true).setNbLinesWithErrorsToStop(3).build(),
						ObjetRg2.class);
		InputStream stream = new StringStream("att1,att2\nRG2,RG2\nRG2,RG1\nRG1,RG2\nRG1,RG3");

		Resultat<ObjetRg2> resultat = moteurRg2.parseInputStream(stream, ObjetRg2.class);
		assertNotNull(resultat);
		assertEquals(1, resultat.getObjets().size());
		assertEquals(3, resultat.getErreurs().size());
	}

	public static class ValidatorPbCreation extends ValidatorCsv {

		public ValidatorPbCreation(String unTruc) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * fr.ybo.moteurcsv.validator.ValidatorCsv#validate(java.lang.String)
		 */
		@Override
		public void validate(String champ) throws ValidateException {
		}

	}

	@CsvFile
	public static class ObjetPbCreationValidator {
		@CsvValidation(ValidatorPbCreation.class)
		@CsvColumn("att")
		public String att;
	}

	@Test
	public void testPbCreationValidator() {
		try {
			new MoteurCsv(ObjetPbCreationValidator.class);
		} catch (MoteurCsvException exception) {
			assertTrue(exception.getMessage().contains(ValidatorPbCreation.class.getSimpleName()));
		}
	}

}
