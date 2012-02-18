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
import fr.ybo.moteurcsv.factory.AbstractEcritureCsv;
import fr.ybo.moteurcsv.factory.AbstractLectureCsv;
import fr.ybo.moteurcsv.factory.DefaultGestionnaireCsvFactory;
import fr.ybo.moteurcsv.factory.GestionnaireCsvFactory;
import fr.ybo.moteurcsv.modele.ChampCsv;
import fr.ybo.moteurcsv.modele.ClassCsv;
import fr.ybo.moteurcsv.modele.InsertInList;
import fr.ybo.moteurcsv.modele.InsertObject;

/**
 * Moteur de lecture et écriture de fichier CSV.<br/>
 * 
 * @author ybonnel
 * 
 */
public class MoteurCsv {

	private final Map<Class<?>, ClassCsv> mapFileClasses = new HashMap<Class<?>, ClassCsv>();

	private String[] enteteCourante;

	private ClassCsv classCourante;

	private GestionnaireCsvFactory factory;

	public void setFactory(GestionnaireCsvFactory factory) {
		this.factory = factory;
	}

	public MoteurCsv(Iterable<Class<?>> classes) {
		factory = new DefaultGestionnaireCsvFactory();
		for (Class<?> clazz : classes) {
			scannerClass(clazz);
		}
	}

	private AbstractLectureCsv lecteurCsv;

	/**
	 * Crée un objet à partir de la ligne courante du csv.
	 * {@link MoteurCsv#nouveauFichier(Reader, Class)} doit être appelé avant.
	 * 
	 * @return l'ojet créer.
	 */
	protected Object creerObjet() {
		if (classCourante == null) {
			throw new MoteurCsvException(
					"La méthode creerObjet a étée appelée sans que la méthode nouveauFichier n'est été appelée.");
		}
		String[] champs = null;
		try {
			champs = lecteurCsv.readLine();
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

	protected void nouveauFichier(Reader reader, Class<?> clazz) {
		classCourante = mapFileClasses.get(clazz);
		if (classCourante == null) {
			throw new MoteurCsvException("La class " + clazz.getSimpleName() + " n'est pas gérée");
		}
		lecteurCsv = factory.createReaderCsv(reader, classCourante.getSeparateurWithoutEscape());
		try {
			enteteCourante = lecteurCsv.readLine();
		} catch (IOException e) {
			throw new MoteurCsvException(e);
		}
		if (Character.isIdentifierIgnorable(enteteCourante[0].charAt(0))) {
			enteteCourante[0] = enteteCourante[0].substring(1);
		}
	}

	private void closeLecteurCourant() {
		if (lecteurCsv != null) {
			try {
				lecteurCsv.close();
			} catch (IOException ignore) {
			}
			lecteurCsv = null;
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
		nouveauFichier(reader, clazz);
		Objet objet = (Objet) creerObjet();
		while (objet != null) {
			insert.insertObject(objet);
			objet = (Objet) creerObjet();
		}
		closeLecteurCourant();
	}

	protected void scannerClass(Class<?> clazz) {
		FichierCsv fichierCsv = clazz.getAnnotation(FichierCsv.class);
		if (fichierCsv == null) {
			throw new MoteurCsvException("Annotation FichierCsv non présente sur la classe " + clazz.getSimpleName());
		}
		if (mapFileClasses.get(clazz) != null) {
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
		mapFileClasses.put(clazz, classCsv);
	}

	private <Objet> void writeLigne(AbstractEcritureCsv writerCsv, List<String> nomChamps, ClassCsv classCsv,
			Objet objet) throws IllegalAccessException {
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
			final ClassCsv classCsv = mapFileClasses.get(clazz);
			AbstractEcritureCsv writerCsv = factory.createWriterCsv(writer, classCsv.getSeparateurWithoutEscape());
			try {
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
