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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
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
	
	private AbstractLectureCsv lecteurCsv;

	/**
	 * Crée un objet à partir de la ligne courante du csv.
	 * {@link MoteurCsv#nouveauFichier(Reader, String)} doit être appelé avant.
	 * @return l'ojet créer.
	 */
	public Object creerObjet() {
		return creerObjet(null);
	}

	/**
	 * Ancienne signature.
	 * @param ligne la ligne à parser.
	 * @return l'objet.
	 * @deprecated use {@link MoteurCsv#creerObjet()}.
	 */
	public Object creerObjet(String ligne) {
		if (classCourante == null) {
			throw new MoteurCsvException(
					"La méthode creerObjet a étée appelée sans que la méthode nouveauFichier n'est été appelée.");
		}
		String[] champs = null;
		try {
			if (ligne == null) {
				champs = lecteurCsv.readLine();
			} else {
				champs = LectureSpecifiqueCsv.splitLine(ligne, classCourante.getSeparateurWithoutEscape());
			}
			if (champs == null) {
				return null;
			}
			Object objetCsv = classCourante.getContructeur().newInstance((Object[]) null);
			for (int numChamp = 0; numChamp < champs.length; numChamp++) {
				String champ = champs[numChamp];
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
					+ classCourante.getClazz().getSimpleName() + " pour la ligne " + champs, e);
		}
	}
	
	protected AbstractLectureCsv creerReaderCsv(Reader reader) {
		return new LectureOpenCsv(reader, classCourante.getSeparateurWithoutEscape());
	}
	
	protected AbstractEcritureCsv creerWriterCsv(Writer writer) {
		return new EcritureOpenCsv(writer, classCourante.getSeparateurWithoutEscape());
	}

	/**
	 * Ancienne signature pour la compatibilité.
	 * @param nomFichier nom du fichier.
	 * @param entete entete.
	 * @deprecated use {@link MoteurCsv#nouveauFichier(Reader, String)}.
	 */
	public void nouveauFichier(String nomFichier, String entete) {
		classCourante = mapFileClasses.get(nomFichier);
		if (classCourante == null) {
			throw new MoteurCsvException("Le fichier " + nomFichier + " n'as pas de classe associée");
		}
		enteteCourante = LectureSpecifiqueCsv.splitLine(entete, classCourante.getSeparateurWithoutEscape());
		if (Character.isIdentifierIgnorable(enteteCourante[0].charAt(0))) {
			enteteCourante[0] = enteteCourante[0].substring(1);
		}
	}

	/**
	 * Première méthode à appeler pour la lecture d'un fichier.
	 * @param reader reader.
	 * @param nomFichier nom du fichier.
	 */
	public void nouveauFichier(Reader reader, String nomFichier) {
		classCourante = mapFileClasses.get(nomFichier);
		if (classCourante == null) {
			throw new MoteurCsvException("Le fichier " + nomFichier + " n'as pas de classe associée");
		}
		lecteurCsv = creerReaderCsv(reader); 
		try {
			enteteCourante = lecteurCsv.readLine();
		} catch (IOException e) {
			throw new MoteurCsvException(e);
		}
		if (Character.isIdentifierIgnorable(enteteCourante[0].charAt(0))) {
			enteteCourante[0] = enteteCourante[0].substring(1);
		}
	}
	
	public void closeFichierCourant() {
		if (lecteurCsv != null) {
			try {
				lecteurCsv.close();
			} catch (IOException ignore) {
			}
			lecteurCsv = null;
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
	public <Objet> void parseFileAndInsert(Reader reader, Class<Objet> clazz, InsertObject<Objet> insert) {
		nouveauFichier(reader, clazz.getAnnotation(FichierCsv.class).value());
		Objet objet = (Objet) creerObjet();
		while (objet != null) {
			insert.insertObject(objet);
			objet = (Objet) creerObjet();
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

	private <Objet> void writeLigne(AbstractEcritureCsv writerCsv, List<String> nomChamps, ClassCsv classCsv,
			Objet objet)
 throws IllegalAccessException {
		List<String> champs = new ArrayList<String>();
		for (String nomChamp : nomChamps) {
			ChampCsv champCsv = classCsv.getChampCsv(nomChamp);
			champCsv.getField().setAccessible(true);
			Object valeur = champCsv.getField().get(objet);
			champCsv.getField().setAccessible(false);
			if (valeur != null) {
				champs.add(champCsv.getNewAdapterCsv().toString(valeur));
			} else {
				champs.add(null);
			}
		}
		writerCsv.writeLine(champs);
	}

	public <Objet> void writeFile(Writer writer, Iterable<Objet> objets, Class<Objet> clazz) {
		try {
			AbstractEcritureCsv writerCsv = creerWriterCsv(writer);
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
				writerCsv.writeLine(nomChamps);
				for (Objet objet : objets) {
					writeLigne(writerCsv, nomChamps, classCsv, objet);
				}
			} finally {
				writerCsv.close();
			}
		} catch (Exception exception) {
			throw new MoteurCsvException(exception);
		}
	}
}
