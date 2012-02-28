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
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.ybo.moteurcsv.annotation.BaliseCsv;
import fr.ybo.moteurcsv.annotation.FichierCsv;
import fr.ybo.moteurcsv.annotation.Validation;
import fr.ybo.moteurcsv.exception.MoteurCsvException;
import fr.ybo.moteurcsv.factory.AbstractReaderCsv;
import fr.ybo.moteurcsv.factory.AbstractWriterCsv;
import fr.ybo.moteurcsv.factory.DefaultGestionnaireCsvFactory;
import fr.ybo.moteurcsv.factory.GestionnaireCsvFactory;
import fr.ybo.moteurcsv.modele.ChampCsv;
import fr.ybo.moteurcsv.modele.ClassCsv;
import fr.ybo.moteurcsv.modele.Erreur;
import fr.ybo.moteurcsv.modele.InsertInList;
import fr.ybo.moteurcsv.modele.InsertObject;
import fr.ybo.moteurcsv.modele.Parametres;
import fr.ybo.moteurcsv.modele.Resultat;
import fr.ybo.moteurcsv.validator.ValidatorCsv;

/**
 * Moteur de lecture et écriture de fichier CSV.<br/>
 * Voici des exemple d'utilisation :
 * <ul>
 * <li>Construction du moteur :<br/>
 * {@code MoteurCsv moteur = new MoteurCsv(ObjetCsv.class);}</li>
 * <li>Lecture d'un fichier CSV :<br/>
 * {@code List<ObjetCsv> objets = moteur.parseInputStream(stream, ObjetCsv.class);}
 * </li>
 * <li>Ecriture d'un fichier CSV :<br/>
 * {@code moteur.writeFile(new FileWriter(file), objets, ObjetCsv.class);}</li>
 * </ul>
 * 
 * @author ybonnel
 * 
 */
public class MoteurCsv {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger("MoteurCsv");

	/**
	 * Map des classes gérées.
	 */
	private final Map<Class<?>, ClassCsv> mapClasses = new HashMap<Class<?>, ClassCsv>();

	/**
	 * Entete courante.
	 */
	private String[] enteteCourante;

	/**
	 * Class courante.
	 */
	private ClassCsv classCourante;

	/**
	 * Factory fournissant les reader et writer csv.
	 */
	private GestionnaireCsvFactory factory;

	/**
	 * Permet d'utiliser autre chose que open-csv pour la lecture et écriture.
	 * 
	 * @param factory
	 *            factory autre que la factory par défaut.
	 */
	public void setFactory(GestionnaireCsvFactory factory) {
		this.factory = factory;
	}

	/**
	 * Paramètres du moteur.
	 */
	private Parametres parametres = new Parametres();

	/**
	 * @return les paramètres du moteur.
	 */
	public Parametres getParametres() {
		return parametres;
	}

	/**
	 * Constructeur du moteur.
	 * 
	 * @param classes
	 *            liste des classes à gérer.
	 */
	public MoteurCsv(Class<?>... classes) {
		factory = new DefaultGestionnaireCsvFactory();
		for (Class<?> clazz : classes) {
			scannerClass(clazz);
		}
	}

	/**
	 * Lecteur CSV.
	 */
	private AbstractReaderCsv lecteurCsv;

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

	/**
	 * Démarre la lecture d'une nouveau fichier.
	 * 
	 * @param reader
	 *            le fichier.
	 * @param clazz
	 *            la class associée.
	 */
	protected void nouveauFichier(Reader reader, Class<?> clazz) {
		classCourante = mapClasses.get(clazz);
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

	/**
	 * Ferme le lecteur courant.
	 */
	private void closeLecteurCourant() {
		if (lecteurCsv != null) {
			try {
				lecteurCsv.close();
			} catch (IOException exception) {
				LOGGER.log(Level.WARNING, "Erreur lors de la fermeture du LecteurCsv", exception);
			}
			lecteurCsv = null;
		}
	}

	/**
	 * Parse un inputStream représentant un fichier CSV pour le transformer en
	 * liste de <Objet>.
	 * 
	 * @param <Objet>
	 *            Objet associé au CSV.
	 * @param intputStream
	 *            inputStream représentant le fichier CSV.
	 * @param clazz
	 *            classe de l'objet associé au CSV.
	 * @return la liste d'<Objet> représentant les enregistrements du fichier
	 *         CSV.
	 */
	public <Objet> Resultat<Objet> parseInputStream(InputStream intputStream, Class<Objet> clazz) {
		Resultat<Objet> resultat = new Resultat<Objet>();
		resultat.getErreurs().addAll(
				parseFileAndInsert(new BufferedReader(new InputStreamReader(intputStream)), clazz,
						new InsertInList<Objet>(resultat.getObjets())));
		return resultat;
	}

	/**
	 * Permet de parser un fichier CSV tout en effectuant un traitement à chaque
	 * enregistrement.
	 * 
	 * @param <Objet>
	 *            Objet associé au CSV.
	 * @param reader
	 *            le Reader représentant le fichier CSV.
	 * @param clazz
	 *            classe de l'objet associé au CSV.
	 * @param insert
	 *            traitement à éffectuer pour chaque enregistrement.
	 * @return les erreurs rencontrées.
	 */
	@SuppressWarnings("unchecked")
	public <Objet> List<Erreur> parseFileAndInsert(Reader reader, Class<Objet> clazz, InsertObject<Objet> insert) {
		List<Erreur> erreurs = new ArrayList<Erreur>();
		nouveauFichier(reader, clazz);
		Objet objet = (Objet) creerObjet();
		while (objet != null) {
			insert.insertObject(objet);
			objet = (Objet) creerObjet();
		}
		closeLecteurCourant();
		return erreurs;
	}

	/**
	 * Scanne une class pour la gérer dans le moteur.
	 * 
	 * @param clazz
	 *            classe à scanner.
	 */
	protected void scannerClass(Class<?> clazz) {
		FichierCsv fichierCsv = clazz.getAnnotation(FichierCsv.class);
		if (fichierCsv == null) {
			throw new MoteurCsvException("Annotation FichierCsv non présente sur la classe " + clazz.getSimpleName());
		}
		if (mapClasses.get(clazz) != null) {
			return;
		}
		ClassCsv classCsv = new ClassCsv(fichierCsv.separateur(), clazz);
		for (Field field : clazz.getDeclaredFields()) {
			BaliseCsv baliseCsv = field.getAnnotation(BaliseCsv.class);
			Validation validation = field.getAnnotation(Validation.class);
			if (baliseCsv != null) {
				Class<? extends ValidatorCsv> validator = validation == null ? null : validation.value();
				classCsv.setChampCsv(baliseCsv.value(),
						new ChampCsv(baliseCsv.adapter(), validator, field, baliseCsv.obligatoire()));
				classCsv.putOrdre(baliseCsv.value(), baliseCsv.ordre());
			}
		}
		mapClasses.put(clazz, classCsv);
	}

	/**
	 * Ecrit une ligne dans le ficheir CSV.
	 * 
	 * @param <Objet>
	 *            Objet associé au CSV.
	 * @param writerCsv
	 *            writer à utiliser.
	 * @param nomChamps
	 *            liste des champs représentant l'entête du CSV.
	 * @param classCsv
	 *            classe associée au fichier CSV.
	 * @param objet
	 *            objet à écrire dans le CSV.
	 * @throws IllegalAccessException
	 *             ne doit pas arriver.
	 */
	private <Objet> void writeLigne(AbstractWriterCsv writerCsv, List<String> nomChamps, ClassCsv classCsv,
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

	/**
	 * Ecrit un fichier CSV à partir d'une liste d'objet.
	 * 
	 * @param <Objet>
	 *            Objet associé au CSV.
	 * @param writer
	 *            writer représentant le fichier CSV.
	 * @param objets
	 *            liste des objets à écrire dans le fichier CSV.
	 * @param clazz
	 *            Classe associée au fichier CSV.
	 */
	public <Objet> void writeFile(Writer writer, Iterable<Objet> objets, Class<Objet> clazz) {
		try {
			final ClassCsv classCsv = mapClasses.get(clazz);
			AbstractWriterCsv writerCsv = factory.createWriterCsv(writer, classCsv.getSeparateurWithoutEscape());
			try {
				List<String> nomChamps = new ArrayList<String>();
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
