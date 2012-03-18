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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.ybo.moteurcsv.annotation.BaliseCsv;
import fr.ybo.moteurcsv.annotation.FichierCsv;
import fr.ybo.moteurcsv.annotation.Validation;
import fr.ybo.moteurcsv.exception.NombreErreurDepasseException;
import fr.ybo.moteurcsv.modele.Erreur;
import fr.ybo.moteurcsv.modele.ParametresMoteur;
import fr.ybo.moteurcsv.modele.Resultat;
import fr.ybo.moteurcsv.validator.ValidatorEmail;

/**
 * Test de {@link ValidatorEmail}.
 */
public class ValidatorEmailTest {

	@FichierCsv
	public static class ObjetEmail {
		@BaliseCsv("att")
		public String att;

		@Validation(ValidatorEmail.class)
		@BaliseCsv("email")
		public String email;
	}

	@Test
	public void testEmail() throws NombreErreurDepasseException {
		StringStream stream =
				new StringStream("att,email\n" + ",\n" + ",nonvalide@tutu\n" + ",nonvalide\n" + ",@nonvalide.fr\n"
						+ ",valide@valide.fr");
		MoteurCsv moteur =
				new MoteurCsv(ParametresMoteur.createBuilder().setNbLinesWithErrorsToStop(999).build(), ObjetEmail.class);
		Resultat<ObjetEmail> resultat = moteur.parseInputStream(stream, ObjetEmail.class);
		assertNotNull(resultat);
		assertEquals(2, resultat.getObjets().size());
		assertNull(resultat.getObjets().get(0).email);
		assertEquals("valide@valide.fr", resultat.getObjets().get(1).email);

		assertEquals(3, resultat.getErreurs().size());
		Erreur erreur1 = resultat.getErreurs().get(0);
		assertEquals(",nonvalide@tutu", erreur1.getLigneCsv());
		assertEquals(1, erreur1.getMessages().size());
		assertTrue(erreur1.getMessages().get(0).contains("nonvalide@tutu"));

		Erreur erreur2 = resultat.getErreurs().get(1);
		assertEquals(",nonvalide", erreur2.getLigneCsv());
		assertEquals(1, erreur2.getMessages().size());
		assertTrue(erreur2.getMessages().get(0).contains("nonvalide"));

		Erreur erreur3 = resultat.getErreurs().get(2);
		assertEquals(",@nonvalide.fr", erreur3.getLigneCsv());
		assertEquals(1, erreur3.getMessages().size());
		assertTrue(erreur3.getMessages().get(0).contains("@nonvalide.fr"));
	}

}
