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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ybo.moteurcsv.annotation.BaliseCsv;
import fr.ybo.moteurcsv.annotation.FichierCsv;
import fr.ybo.moteurcsv.exception.MoteurCsvException;
import fr.ybo.moteurcsv.modele.ChampCsv;
import fr.ybo.moteurcsv.modele.ClassCsv;

public class MoteurCsv {

	private final Map<String, ClassCsv> mapFileClasses = new HashMap<String, ClassCsv>(5);

	private String[] enteteCourante;

	private ClassCsv classCourante;

	public MoteurCsv(Iterable<Class<?>> classes) {
		for (Class<?> clazz : classes) {
			scannerClass(clazz);
		}
	}

	public Class<?> getClassByFileName(String fileName) {
		return mapFileClasses.containsKey(fileName) ? mapFileClasses.get(fileName).getClazz() : null;
	}

	private List<String> splitLigne(String ligne, String separateur) {
		List<String> champs = new ArrayList<String>();
		int indiceDebutChaine = -1;
		boolean enCours = false;
		boolean guillemetEnCours = false;
		boolean guillemetTermine = false;
		for (int indice = 0; indice < ligne.length(); indice++) {
			if (!enCours) {
				if (ligne.charAt(indice) == separateur.charAt(0)) {
					champs.add("");
					guillemetEnCours = false;
					guillemetTermine = false;
					indiceDebutChaine = -1;
				} else if (ligne.charAt(indice) == '"') {
					guillemetEnCours = true;
				} else {
					enCours = true;
					indiceDebutChaine = indice;
				}
			} else {
				if (guillemetEnCours) {
					if ('"' == ligne.charAt(indice)) {
						guillemetEnCours = false;
						guillemetTermine = true;
					}
				} else if (ligne.charAt(indice) == separateur.charAt(0)) {
					if (indiceDebutChaine == -1) {
						champs.add("");
					} else {
						int indiceFin = indice;
						if (guillemetTermine) {
							indiceFin = indice - 1;
						}
						champs.add(ligne.substring(indiceDebutChaine, indiceFin));
						guillemetEnCours = false;
						guillemetTermine = false;
						enCours = false;
						indiceDebutChaine = -1;
					}
				}
			}
		}
		if (enCours) {
			if (indiceDebutChaine == -1) {
				champs.add("");
			} else {
				int indiceFin = ligne.length();
				if (guillemetTermine) {
					indiceFin = ligne.length() - 1;
				}
				champs.add(ligne.substring(indiceDebutChaine, indiceFin));
			}
		}
		return champs;
	}

	public Object creerObjet(String ligne) {
		if (classCourante == null) {
			throw new MoteurCsvException(
					"La méthode creerObjet a étée appelée sans que la méthode nouveauFichier n'est été appelée.");
		}
		try {
			Object objetCsv = classCourante.getContructeur().newInstance((Object[]) null);
			List<String> champs = splitLigne(ligne, classCourante.getSeparateurWithoutEscape());
			for (int numChamp = 0; numChamp < champs.size(); numChamp++) {
				String champ = champs.get(numChamp);
				if (champ != null && !"".equals(champ)) {
					String nomChamp = enteteCourante[numChamp];
					ChampCsv champCsv = classCourante.getChampCsv(nomChamp);
					if (champCsv != null) {
						champCsv.getField().setAccessible(true);
						champCsv.getField().set(objetCsv, champCsv.getNewAdapterCsv().parse(champ));
						champCsv.getField().setAccessible(false);
					}
				}
			}
			return objetCsv;
		} catch (Exception e) {
			throw new MoteurCsvException("Erreur à l'instanciation de la class "
					+ classCourante.getClazz().getSimpleName() + " pour la ligne " + ligne, e);
		}
	}

	public void nouveauFichier(String nomFichier, String entete) {
		classCourante = mapFileClasses.get(nomFichier);
		if (classCourante == null) {
			throw new MoteurCsvException("Le fichier " + nomFichier + " n'as pas de classe associée");
		}
		enteteCourante = entete.split(classCourante.getSeparateur());
		if (Character.isIdentifierIgnorable(enteteCourante[0].charAt(0))) {
			enteteCourante[0] = enteteCourante[0].substring(1);
		}
	}

	public static interface InsertObject<Objet> {
		public void insertObject(Objet objet);
	}

	private static class InsertInList<Objet> implements InsertObject<Objet> {

		private List<Objet> objets;

		public InsertInList(List<Objet> objets) {
			this.objets = objets;
		}

		@Override
		public void insertObject(Objet objet) {
			objets.add(objet);
		}
	}

	public <Objet> List<Objet> parseInputStream(InputStream intputStream, Class<Objet> clazz) {
		List<Objet> objets = new ArrayList<Objet>();
		parseFileAndInsert(new BufferedReader(new InputStreamReader(intputStream)), clazz, new InsertInList<Objet>(
				objets));
		return objets;
	}

	@SuppressWarnings("unchecked")
	public <Objet> void parseFileAndInsert(BufferedReader bufReader, Class<Objet> clazz, InsertObject<Objet> insert) {
		try {
			nouveauFichier(clazz.getAnnotation(FichierCsv.class).value(), bufReader.readLine());
			String ligne = bufReader.readLine();
			while (ligne != null) {
				insert.insertObject((Objet) creerObjet(ligne));
				ligne = bufReader.readLine();
			}
		} catch (IOException ioException) {
			throw new MoteurCsvException(ioException);
		}
	}

	private void scannerClass(Class<?> clazz) {
		FichierCsv fichierCsv = clazz.getAnnotation(FichierCsv.class);
		if (fichierCsv == null) {
			throw new MoteurCsvException("Annotation FichierCsv non présente sur la classe " + clazz.getSimpleName());
		}
		if (mapFileClasses.get(fichierCsv.value()) != null) {
			return;
		}
		ClassCsv classCsv = new ClassCsv(fichierCsv.separateur(), clazz);
		for (Field field : clazz.getDeclaredFields()) {
			BaliseCsv baliseCsv = field.getAnnotation(BaliseCsv.class);
			if (baliseCsv != null) {
				classCsv.setChampCsv(baliseCsv.value(), new ChampCsv(baliseCsv.adapter(), field));
				classCsv.putOrdre(baliseCsv.value(), baliseCsv.ordre());
			}
		}
		mapFileClasses.put(fichierCsv.value(), classCsv);
	}

	private void writeEntete(BufferedWriter bufWriter, Iterable<String> nomChamps, ClassCsv classCsv)
			throws IOException {
		boolean first = true;
		for (String nomChamp : nomChamps) {
			if (!first) {
				bufWriter.write(classCsv.getSeparateurWithoutEscape());
			}
			bufWriter.write(nomChamp);
			first = false;
		}
		bufWriter.write('\n');
	}

	private <Objet> void writeLigne(BufferedWriter bufWriter, Iterable<String> nomChamps, ClassCsv classCsv, Objet objet)
			throws IOException, IllegalAccessException {
		boolean first = true;
		for (String nomChamp : nomChamps) {
			if (!first) {
				bufWriter.write(classCsv.getSeparateurWithoutEscape());
			}
			ChampCsv champCsv = classCsv.getChampCsv(nomChamp);
			champCsv.getField().setAccessible(true);
			Object valeur = champCsv.getField().get(objet);
			champCsv.getField().setAccessible(false);
			if (valeur != null) {
				String champ = champCsv.getNewAdapterCsv().toString(valeur);
				if (champ.contains("\"")) {
					champ = "\"" + champ + "\"";
				}
				bufWriter.write(champ);
			}
			first = false;
		}
		bufWriter.write('\n');
	}

	public <Objet> void writeFile(File file, Iterable<Objet> objets, Class<Objet> clazz) {
		try {
			BufferedWriter bufWriter = new BufferedWriter(new FileWriter(file));
			try {
				final ClassCsv classCsv = mapFileClasses.get(clazz.getAnnotation(FichierCsv.class).value());
				List<String> nomChamps = new ArrayList<String>(10);
				for (String champ : classCsv.getNomChamps()) {
					nomChamps.add(champ);
				}
				Collections.sort(nomChamps, new Comparator<String>() {
					public int compare(String o1, String o2) {
						int thisVal = classCsv.getOrdre(o1);
						int anotherVal = classCsv.getOrdre(o2);
						return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
					}
				});
				writeEntete(bufWriter, nomChamps, classCsv);
				for (Objet objet : objets) {
					writeLigne(bufWriter, nomChamps, classCsv, objet);
				}
			} finally {
				bufWriter.close();
			}
		} catch (Exception exception) {
			throw new MoteurCsvException(exception);
		}
	}
}
