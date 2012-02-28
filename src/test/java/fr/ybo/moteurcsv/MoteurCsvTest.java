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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.ybo.moteurcsv.adapter.AdapterBoolean;
import fr.ybo.moteurcsv.adapter.AdapterDouble;
import fr.ybo.moteurcsv.adapter.AdapterInteger;
import fr.ybo.moteurcsv.adapter.AdapterString;
import fr.ybo.moteurcsv.adapter.AdapterTime;
import fr.ybo.moteurcsv.annotation.BaliseCsv;
import fr.ybo.moteurcsv.annotation.FichierCsv;
import fr.ybo.moteurcsv.exception.MoteurCsvException;
import fr.ybo.moteurcsv.factory.AbstractReaderCsv;
import fr.ybo.moteurcsv.factory.AbstractWriterCsv;
import fr.ybo.moteurcsv.factory.GestionnaireCsvFactory;

/**
 * Classe de test pour le MoteurCsv.
 * 
 * @author ybonnel
 * 
 */
public class MoteurCsvTest {

	/**
	 * Objet permettant de tester le moteur.
	 * 
	 * @author ybonnel
	 * 
	 */
	@FichierCsv(separateur = "\\|")
	public static class ObjetCsv {

		/**
		 * Attribut simple.
		 */
		@BaliseCsv(value = "att_1", ordre = 0)
		private String attribut1;

		/**
		 * Attribut de type Boolean.
		 */
		@BaliseCsv(value = "att_2", ordre = 1, adapter = AdapterBoolean.class)
		private Boolean attribut2;

		/**
		 * Attribut de type Double.
		 */
		@BaliseCsv(value = "att_3", ordre = 2, adapter = AdapterDouble.class)
		private Double attribut3;

		/**
		 * Attribut de type Integer.
		 */
		@BaliseCsv(value = "att_4", ordre = 5, adapter = AdapterInteger.class)
		private Integer attribut4;

		/**
		 * Attribut simple avec un adapter.
		 */
		@BaliseCsv(value = "att_5", ordre = 3, adapter = AdapterString.class)
		private String attribut5;

		/**
		 * Attribut de type Time.
		 */
		@BaliseCsv(value = "att_6", ordre = 6, adapter = AdapterTime.class)
		private Integer attribut6;

		/**
		 * Equals.
		 * 
		 * @param att1
		 *            att1.
		 * @param att2
		 *            att2.
		 * @param att3
		 *            att3.
		 * @param att4
		 *            att4.
		 * @param att5
		 *            att5.
		 * @param att6
		 *            att6.
		 * @return true si equals.
		 */
		protected boolean equals(String att1, Boolean att2, Double att3, Integer att4, String att5, Integer att6) {
			return ((att1 == null && attribut1 == null || att1 != null && attribut1 != null && att1.equals(attribut1))
					&& (att2 == null && attribut2 == null || att2 != null && attribut2 != null
							&& att2.equals(attribut2))
					&& (att3 == null && attribut3 == null || att3 != null && attribut3 != null
							&& att3.equals(attribut3))
					&& (att4 == null && attribut4 == null || att4 != null && attribut4 != null
							&& att4.equals(attribut4))
					&& (att5 == null && attribut5 == null || att5 != null && attribut5 != null
							&& att5.equals(attribut5)) && (att6 == null && attribut6 == null || att6 != null
					&& attribut6 != null && att6.equals(attribut6)));
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((attribut1 == null) ? 0 : attribut1.hashCode());
			result = prime * result + ((attribut2 == null) ? 0 : attribut2.hashCode());
			result = prime * result + ((attribut3 == null) ? 0 : attribut3.hashCode());
			result = prime * result + ((attribut4 == null) ? 0 : attribut4.hashCode());
			result = prime * result + ((attribut5 == null) ? 0 : attribut5.hashCode());
			result = prime * result + ((attribut6 == null) ? 0 : attribut6.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ObjetCsv other = (ObjetCsv) obj;
			if (attribut1 == null) {
				if (other.attribut1 != null)
					return false;
			} else if (!attribut1.equals(other.attribut1))
				return false;
			if (attribut2 == null) {
				if (other.attribut2 != null)
					return false;
			} else if (!attribut2.equals(other.attribut2))
				return false;
			if (attribut3 == null) {
				if (other.attribut3 != null)
					return false;
			} else if (!attribut3.equals(other.attribut3))
				return false;
			if (attribut4 == null) {
				if (other.attribut4 != null)
					return false;
			} else if (!attribut4.equals(other.attribut4))
				return false;
			if (attribut5 == null) {
				if (other.attribut5 != null)
					return false;
			} else if (!attribut5.equals(other.attribut5))
				return false;
			if (attribut6 == null) {
				if (other.attribut6 != null)
					return false;
			} else if (!attribut6.equals(other.attribut6))
				return false;
			return true;
		}

	}

	/**
	 * Moteur.
	 */
	private MoteurCsv moteur = null;

	/**
	 * Inputstream.
	 */
	private InputStream stream = null;

	/**
	 * Entete.
	 */
	private final static String ENTETE_654321 = "att_6|att_5|att_4|att_3|att_2|att_1";

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		moteur = new MoteurCsv(ObjetCsv.class, ObjetCsv.class);
		stream = new InputStream() {
			String chaine = ENTETE_654321 + "\n01:30|String1|5|8.0|1|String2\n" + "|String1|5|8.0|1|String2\n"
					+ "\n01:30||5|8.0|1|String2\n" + "01:30|String1||8.0|1|String2\n" + "01:30|String1|5||1|String2\n"
					+ "01:30|String1|5|8.0||String2\n" + "01:30|String1|5|8.0|1|\n";
			private int count = 0;

			@Override
			public int read() throws IOException {
				if (count >= chaine.length()) {
					return -1;
				}
				return chaine.charAt(count++);
			}
		};
	}

	@Test
	public void testParseInputStream() throws IOException {

		List<ObjetCsv> objets = moteur.parseInputStream(stream, ObjetCsv.class).getObjets();
		assertEquals(7, objets.size());
		assertTrue(objets.get(0).equals("String2", true, 8.0, 5, "String1", 90));
		assertTrue(objets.get(1).equals("String2", true, 8.0, 5, "String1", null));
		assertTrue(objets.get(2).equals("String2", true, 8.0, 5, null, 90));
		assertTrue(objets.get(3).equals("String2", true, 8.0, null, "String1", 90));
		assertTrue(objets.get(4).equals("String2", true, null, 5, "String1", 90));
		assertTrue(objets.get(5).equals("String2", null, 8.0, 5, "String1", 90));
		assertTrue(objets.get(6).equals(null, true, 8.0, 5, "String1", 90));

		File file = File.createTempFile("objet_csv", "txt");

		moteur.writeFile(new FileWriter(file), objets, ObjetCsv.class);

		List<ObjetCsv> newObjets = moteur.parseInputStream(new FileInputStream(file), ObjetCsv.class).getObjets();
		assertEquals(objets, newObjets);

	}

	@Test
	public void testOtherFactory() {

		moteur.setFactory(new GestionnaireCsvFactory() {

			public AbstractWriterCsv createWriterCsv(final Writer writer, char separator) {
				return new AbstractWriterCsv() {

					public void close() throws IOException {
						writer.close();
					}

					@Override
					public void writeLine(List<String> champs) {
						try {
							writer.append("coucou\n");
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				};
			}

			public AbstractReaderCsv createReaderCsv(final Reader reader, char separator) {
				return new AbstractReaderCsv() {

					boolean first = true;

					public void close() throws IOException {
						reader.close();
					}

					public String[] readLine() throws IOException {
						if (first) {
							first = false;
							return ENTETE_654321.split("\\|");
						}
						return null;
					}
				};
			}
		});

		assertTrue(moteur.parseInputStream(stream, ObjetCsv.class).getObjets().isEmpty());

	}

	/**
	 * Tests techniques
	 */

	@Test
	public void testCreerObjet() {
		try {
			moteur.creerObjet();
			fail("Une exception aurait du être levée");
		} catch (MoteurCsvException exception) {
			assertTrue(exception.getMessage().contains("nouveauFichier"));
		}

		moteur.setFactory(new GestionnaireCsvFactory() {
			public AbstractWriterCsv createWriterCsv(Writer writer, char separator) {
				return null;
			}

			public AbstractReaderCsv createReaderCsv(Reader reader, char separator) {
				return new AbstractReaderCsv() {
					public void close() throws IOException {
					}

					private boolean first = true;

					public String[] readLine() throws IOException {
						if (first) {
							first = false;
							return ENTETE_654321.split("\\|");
						}
						throw new IOException();
					}
				};
			}
		});

		moteur.nouveauFichier(new InputStreamReader(stream), ObjetCsv.class);

		try {
			moteur.creerObjet();
			fail("Une exception aurait du être levée");
		} catch (MoteurCsvException exception) {
			assertEquals(IOException.class, exception.getCause().getClass());
			assertTrue(exception.getMessage().contains("ObjetCsv"));
		}
	}

	@Test
	public void testNouveauFichier() {
		try {
			moteur.nouveauFichier(null, String.class);
			fail("Une exception aurait du être levée");
		} catch (MoteurCsvException exception) {
			assertTrue(exception.getMessage().contains("String"));
		}

		moteur.setFactory(new GestionnaireCsvFactory() {
			public AbstractWriterCsv createWriterCsv(Writer writer, char separator) {
				return null;
			}

			public AbstractReaderCsv createReaderCsv(Reader reader, char separator) {
				return new AbstractReaderCsv() {
					public void close() throws IOException {
					}

					public String[] readLine() throws IOException {
						throw new IOException();
					}
				};
			}
		});

		try {
			moteur.nouveauFichier(null, ObjetCsv.class);
			fail("Une exception aurait du être levée");
		} catch (MoteurCsvException exception) {
			assertEquals(IOException.class, exception.getCause().getClass());
		}
	}

	@Test
	public void testScannerClass() {
		try {
			moteur.scannerClass(String.class);
			fail("Une exception aurait du être levée");
		} catch (MoteurCsvException exception) {
			assertTrue(exception.getMessage().contains("String"));
		}
	}

	@Test
	public void testWriteFile() {
		moteur.setFactory(new GestionnaireCsvFactory() {

			public AbstractWriterCsv createWriterCsv(Writer writer, char separator) {
				return new AbstractWriterCsv() {
					public void close() throws IOException {
					}

					public void writeLine(List<String> champs) {
						throw new NullPointerException();
					}
				};
			}

			public AbstractReaderCsv createReaderCsv(Reader reader, char separator) {
				return null;
			}
		});

		try {
			moteur.writeFile(null, new ArrayList<ObjetCsv>(), ObjetCsv.class);
			fail("Une exception aurait du être levée");
		} catch (MoteurCsvException exception) {
			assertEquals(NullPointerException.class, exception.getCause().getClass());
		}
	}

}
