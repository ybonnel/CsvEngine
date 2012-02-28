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

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import fr.ybo.moteurcsv.annotation.BaliseCsv;
import fr.ybo.moteurcsv.annotation.FichierCsv;
import fr.ybo.moteurcsv.modele.Erreur;
import fr.ybo.moteurcsv.modele.Resultat;

/**
 * Tester la validation.
 * <ul>
 * <li>REGLE RG1 : permettre de valider un champ sur son côté obligatoire.</li>
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
	 * @author ybonnel
	 *
	 */
	private final class StringStream extends InputStream {
		private String chaine;
		private int count = 0;

		@Override
		public int read() throws IOException {
			if (count >= chaine.length()) {
				return -1;
			}
			return chaine.charAt(count++);
		}

		public StringStream(String chaine) {
			super();
			this.chaine = chaine;
		}

	}

	@FichierCsv()
	private static class ObjetRg1_1 {
		@BaliseCsv(value = "att1")
		private String att1;
		@BaliseCsv(value = "att2")
		private String att2;
	}

	@FichierCsv()
	private static class ObjetRg1_2 {
		@BaliseCsv(value = "att1", obligatoire = true)
		private String att1;
		@BaliseCsv(value = "att2")
		private String att2;
	}

	@FichierCsv()
	private static class ObjetRg1_3 {
		@BaliseCsv(value = "att1")
		private String att1;
		@BaliseCsv(value = "att2", obligatoire = true)
		private String att2;
	}

	@FichierCsv()
	private static class ObjetRg1_4 {
		@BaliseCsv(value = "att1", obligatoire = true)
		private String att1;
		@BaliseCsv(value = "att2", obligatoire = true)
		private String att2;
	}

	@Before
	public void setup() {
		streamRg1 = new StringStream("att1,att2\nval1,\n,val2\n,");
		moteurRg1 = new MoteurCsv(ObjetRg1_1.class, ObjetRg1_2.class, ObjetRg1_3.class, ObjetRg1_4.class);
	}

	private InputStream streamRg1;
	private MoteurCsv moteurRg1;

	@Test
	public void testRg1_1() {
		// Test avec les deux champs facultatif.
		Resultat<ObjetRg1_1> resultat = moteurRg1.parseInputStream(streamRg1, ObjetRg1_1.class);
		assertNotNull(resultat);
		assertEquals(3, resultat.getObjets().size());
		assertEquals(0, resultat.getErreurs().size());
	}

	@Test
	public void testRg1_2() {
		// Test avec le premier champ obligatoire.
		Resultat<ObjetRg1_2> resultat = moteurRg1.parseInputStream(streamRg1, ObjetRg1_2.class);
		assertNotNull(resultat);
		assertEquals(1, resultat.getObjets().size());
		assertEquals(2, resultat.getErreurs().size());
		Erreur erreur1 = resultat.getErreurs().get(0);
		assertEquals(",val2", erreur1.getLigneCsv());
		assertEquals(1, erreur1.getMessages().size());
		assertTrue(erreur1.getMessages().get(0).contains("obligatoire"));
		assertTrue(erreur1.getMessages().get(0).contains("att1"));
		Erreur erreur2 = resultat.getErreurs().get(1);
		assertEquals(",", erreur2.getLigneCsv());
		assertEquals(1, erreur2.getMessages().size());
		assertTrue(erreur2.getMessages().get(0).contains("obligatoire"));
		assertTrue(erreur2.getMessages().get(0).contains("att1"));
	}

	@Test
	public void testRg1_3() {
		// Test avec le deuxième champ obligatoire.
		Resultat<ObjetRg1_3> resultat = moteurRg1.parseInputStream(streamRg1, ObjetRg1_3.class);
		assertNotNull(resultat);
		assertEquals(1, resultat.getObjets().size());
		assertEquals(2, resultat.getErreurs().size());
		Erreur erreur1 = resultat.getErreurs().get(0);
		assertEquals("val1,", erreur1.getLigneCsv());
		assertEquals(1, erreur1.getMessages().size());
		assertTrue(erreur1.getMessages().get(0).contains("obligatoire"));
		assertTrue(erreur1.getMessages().get(0).contains("att2"));
		Erreur erreur2 = resultat.getErreurs().get(1);
		assertEquals(",", erreur2.getLigneCsv());
		assertEquals(1, erreur2.getMessages().size());
		assertTrue(erreur2.getMessages().get(0).contains("obligatoire"));
		assertTrue(erreur2.getMessages().get(0).contains("att2"));
	}

	@Test
	public void testRg1_4() {
		// Test avec les deux champs obligatoires
		Resultat<ObjetRg1_4> resultat = moteurRg1.parseInputStream(streamRg1, ObjetRg1_4.class);
		assertNotNull(resultat);
		assertEquals(0, resultat.getObjets().size());
		assertEquals(3, resultat.getErreurs().size());
		Erreur erreur1 = resultat.getErreurs().get(0);
		assertEquals("val1,", erreur1.getLigneCsv());
		assertEquals(1, erreur1.getMessages().size());
		assertTrue(erreur1.getMessages().get(0).contains("obligatoire"));
		assertTrue(erreur1.getMessages().get(0).contains("att2"));
		Erreur erreur2 = resultat.getErreurs().get(0);
		assertEquals(",val2", erreur2.getLigneCsv());
		assertEquals(1, erreur2.getMessages().size());
		assertTrue(erreur2.getMessages().get(0).contains("obligatoire"));
		assertTrue(erreur2.getMessages().get(0).contains("att1"));
		Erreur erreur3 = resultat.getErreurs().get(1);
		assertEquals(",", erreur3.getLigneCsv());
		assertEquals(2, erreur3.getMessages().size());
		assertTrue(erreur3.getMessages().get(0).contains("obligatoire"));
		assertTrue(erreur3.getMessages().get(0).contains("att1"));
		assertTrue(erreur3.getMessages().get(1).contains("obligatoire"));
		assertTrue(erreur3.getMessages().get(1).contains("att1"));
	}

	@Test
	public void testRg2() {

	}

	@Test
	public void testRg3() {

	}

	@Test
	public void testRg4() {

	}

}
