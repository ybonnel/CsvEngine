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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.ybo.moteurcsv.annotation.CsvColumn;
import fr.ybo.moteurcsv.annotation.CsvFile;
import fr.ybo.moteurcsv.annotation.CsvValidation;
import fr.ybo.moteurcsv.annotation.CsvValidations;
import fr.ybo.moteurcsv.exception.MoteurCsvException;
import fr.ybo.moteurcsv.exception.CsvErrorsExceededException;
import fr.ybo.moteurcsv.factory.AbstractCsvReader;
import fr.ybo.moteurcsv.factory.AbstractCsvWriter;
import fr.ybo.moteurcsv.factory.DefaultCsvManagerFactory;
import fr.ybo.moteurcsv.factory.CsvManagerFactory;
import fr.ybo.moteurcsv.modele.*;
import fr.ybo.moteurcsv.modele.Error;
import fr.ybo.moteurcsv.validator.ErreurValidation;
import fr.ybo.moteurcsv.validator.ValidateException;

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
	private final Map<Class<?>, CsvClass> mapClasses = new HashMap<Class<?>, CsvClass>();

	/**
	 * Entete courante.
	 */
	private String[] enteteCourante;

	/**
	 * Class courante.
	 */
	private CsvClass currentCsvClass;

	/**
	 * Factory fournissant les reader et writer csv.
	 */
	private CsvManagerFactory factory;

	/**
	 * Permet d'utiliser autre chose que open-csv pour la lecture et écriture.
	 * 
	 * @param factory
	 *            factory autre que la factory par défaut.
	 */
	public void setFactory(CsvManagerFactory factory) {
		this.factory = factory;
	}

	/**
	 * Paramètres du moteur.
	 */
	private final MotorParameters parametres;

	/**
	 * @return les paramètres du moteur.
	 */
	public MotorParameters getParametres() {
		return parametres;
	}

	/**
	 * Constructeur du moteur.
	 * 
	 * @param classes
	 *            liste des classes à gérer.
	 */
	public MoteurCsv(Class<?>... classes) {
		parametres = new MotorParameters();
		factory = new DefaultCsvManagerFactory();
		for (Class<?> clazz : classes) {
			scannerClass(clazz);
		}
	}

	/**
	 * Constructeur du moteur.
	 * 
	 * @param parametres
	 *            parametres du moteur (vous pouvez utiliser:
	 *            {@link fr.ybo.moteurcsv.modele.MotorParameters#createBuilder()} pour plus de facilité).<br/>
	 *            Exemple : Parametres parametres =
	 *            Parameters.createBuilder().setValidation(true).build();
	 * 
	 * @param classes
	 *            liste des classes à gérer.
	 */
	public MoteurCsv(MotorParameters parametres, Class<?>... classes) {
		this.parametres = parametres;
		factory = new DefaultCsvManagerFactory();
		for (Class<?> clazz : classes) {
			scannerClass(clazz);
		}
	}

	/**
	 * Lecteur CSV.
	 */
	private AbstractCsvReader csvReader;

	/**
	 * Permet de recontruire une ligne à partir des champs qui la compose.
	 * 
	 * @param champs
	 *            liste des champs.
	 * @return ligne CSV.
	 */
	private String construireLigne(String[] champs) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (String champ : champs) {
			if (first) {
				first = false;
			} else {
				builder.append(currentCsvClass.getSeparatorWithoutEscape());
			}
			builder.append(champ);
		}
		return builder.toString();
	}

	/**
	 * Permet de setter une valeur d'une champ.
	 * 
	 * @param csvField
	 *            le champ à setter.
	 * @param objetCsv
	 *            l'objet concerné.
	 * @param champ
	 *            la valeur à setter.
	 * @throws ValidateException
	 *             si la valeur n'est pas valide.
	 */
	private void setValeur(CsvField csvField, Object objetCsv, String champ) throws ValidateException {
		csvField.getField().setAccessible(true);
		try {
			csvField.getField().set(objetCsv, csvField.getAdapterCsv().parse(champ));
		} catch (ValidateException exception) {
			throw exception;
		} catch (Exception e) {
			throw new ValidateException("Error à l'assignation", e);
		}
	}

	/**
	 * Crée un objet à partir de la ligne courante du csv.
	 * {@link MoteurCsv#nouveauFichier(Reader, Class)} doit être appelé avant.
	 * 
	 * @return l'ojet créer.
	 * @throws ErreurValidation
	 *             en cas d'erreur de validation.
	 */
	protected Object creerObjet() throws ErreurValidation {
		if (currentCsvClass == null) {
			throw new MoteurCsvException(
					"La méthode creerObjet a étée appelée sans que la méthode nouveauFichier n'est été appelée.");
		}
		String[] champs = readLine();
		if (champs == null) {
			return null;
		}
		ErreurValidation validation = null;
		Object objetCsv = construireObjet();
		for (int numChamp = 0; numChamp < champs.length; numChamp++) {
			validation = traitementChamp(champs, validation, objetCsv, numChamp);
		}
		if (validation != null) {
			throw validation;
		}
		return objetCsv;
	}

	/**
	 * Traitment d'un champ.
	 * 
	 * @param champs
	 *            liste des valeurs de champs.
	 * @param validation
	 *            conteneur d'erreur de validation.
	 * @param objetCsv
	 *            objet à remplir.
	 * @param numChamp
	 *            numéro du champ.
	 * @return conteneur d'erreur de validation.
	 */
	private ErreurValidation traitementChamp(String[] champs, ErreurValidation validation,
			Object objetCsv, int numChamp) {
		String champ = champs[numChamp];
		if (champ != null && !"".equals(champ)) {
			validation = remplirAttribut(champs, validation, objetCsv, numChamp, champ);
		} else if (parametres.hasValidation()) {
			validation = validChampObligatoire(champs, validation, numChamp);
		}
		return validation;
	}

	/**
	 * CsvValidation du caractère mandatory du champ.
	 * 
	 * @param champs
	 *            liste des champs.
	 * @param validation
	 *            conteneur d'erreur de validation.
	 * @param numChamp
	 *            numéro du champ.
	 * @return conteneur d'erreur de validation.
	 */
	private ErreurValidation validChampObligatoire(String[] champs, ErreurValidation validation, int numChamp) {
		CsvField csvField = currentCsvClass.getCsvField(enteteCourante[numChamp]);
		if (csvField != null && csvField.isMandatory()) {
			validation =
					addMessageValidation(champs, validation, enteteCourante[numChamp], new ValidateException(
							"Le champ est mandatory"));
		}
		return validation;
	}

	/**
	 * Valide le champ et rempli l'attribut correspondant au champ.
	 * 
	 * @param champs
	 *            liste des valeurs de champ.
	 * @param validation
	 *            conteneur d'erreur de validation.
	 * @param objetCsv
	 *            objet à remplir.
	 * @param numChamp
	 *            numéro du champ.
	 * @param champ
	 *            valeur du champ.
	 * @return conteneur d'erreur de validation.
	 */
	private ErreurValidation remplirAttribut(String[] champs, ErreurValidation validation, Object objetCsv,
			int numChamp, String champ) {
		String nomChamp = enteteCourante[numChamp];
		CsvField csvField = currentCsvClass.getCsvField(nomChamp);
		if (csvField != null) {
			try {
				if (parametres.hasValidation()) {
					csvField.validate(champ);
				}
				setValeur(csvField, objetCsv, champ);
			} catch (ValidateException exception) {
				validation = addMessageValidation(champs, validation, enteteCourante[numChamp], exception);
			}
		}
		return validation;
	}

	/**
	 * Ajout du message de validation.
	 * 
	 * @param champs
	 *            champs de la ligne CSV.
	 * @param validation
	 *            erreur de validation courante.
	 * @param nomChamp
	 *            nom du champ.
	 * @param exception
	 *            exception de validation.
	 * @return l'erreur de validation.
	 */
	private ErreurValidation addMessageValidation(String[] champs, ErreurValidation validation, String nomChamp,
			ValidateException exception) {
		if (validation == null) {
			validation = new ErreurValidation(construireLigne(champs));
		}
		StringBuilder message = new StringBuilder("Error de validation sur le champ ");
		message.append(nomChamp);
		message.append(" : ");
		message.append(exception.getMessage());
		if (exception.getCause() != null) {
			message.append(" (cause : ");
			message.append(exception.getCause().getMessage());
			message.append(')');
		}
		validation.getError().getMessages().add(message.toString());
		return validation;
	}

	/**
	 * Méthode permettant de construire un objet.
	 * 
	 * @return objet constuit.
	 */
	private Object construireObjet() {
		Object objetCsv = null;
		try {
			objetCsv = currentCsvClass.getConstructor().newInstance((Object[]) null);
		} catch (Exception exception) {
			Throwable myException = exception;
			if (myException instanceof InvocationTargetException) {
				myException = exception.getCause();
			}
			throw new MoteurCsvException("Error à l'instanciation de la class "
					+ currentCsvClass.getClazz().getSimpleName(), myException);
		}
		return objetCsv;
	}

	/**
	 * Lecture d'une ligne.
	 * 
	 * @return ligne lue.
	 */
	private String[] readLine() {
		try {
			return csvReader.readLine();
		} catch (IOException e) {
			throw new MoteurCsvException("Error à la lecture d'une ligne", e);
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
		currentCsvClass = mapClasses.get(clazz);
		if (currentCsvClass == null) {
			throw new MoteurCsvException("La class " + clazz.getSimpleName() + " n'est pas gérée");
		}
		csvReader = factory.createReaderCsv(reader, currentCsvClass.getSeparatorWithoutEscape());
		try {
			enteteCourante = csvReader.readLine();
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
		if (csvReader != null) {
			try {
				csvReader.close();
			} catch (IOException exception) {
				LOGGER.log(Level.WARNING, "Error lors de la fermeture du LecteurCsv", exception);
			}
			csvReader = null;
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
	 * @throws fr.ybo.moteurcsv.exception.CsvErrorsExceededException
	 *             si le nombre d'erreurs rencontrées et suppérieur au nombre
	 *             accepté {@link fr.ybo.moteurcsv.modele.MotorParameters#getNbLinesWithErrorsToStop()}.
	 */
	public <Objet> Result<Objet> parseInputStream(InputStream intputStream, Class<Objet> clazz)
			throws CsvErrorsExceededException {
		Result<Objet> result = new Result<Objet>();
		result.getErrors().addAll(
				parseFileAndInsert(new BufferedReader(new InputStreamReader(intputStream)), clazz,
						new InsertInList<Objet>(result.getObjects())));
		return result;
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
	 * @throws fr.ybo.moteurcsv.exception.CsvErrorsExceededException
	 *             si le nombre d'erreurs rencontrées et suppérieur au nombre
	 *             accepté {@link fr.ybo.moteurcsv.modele.MotorParameters#getNbLinesWithErrorsToStop()}.
	 */
	@SuppressWarnings("unchecked")
	public <Objet> List<Error> parseFileAndInsert(Reader reader, Class<Objet> clazz, InsertObject<Objet> insert)
			throws CsvErrorsExceededException {
		List<fr.ybo.moteurcsv.modele.Error> errors = new ArrayList<Error>();
		try {
			nouveauFichier(reader, clazz);
			Objet objet = null;
			boolean erreurValidation = false;
			do {
				try {
					erreurValidation = false;
					objet = (Objet) creerObjet();
					if (objet != null) {
						insert.insertObject(objet);
					}
				} catch (ErreurValidation exceptionValidation) {
					erreurValidation = true;
					errors.add(exceptionValidation.getError());
					if (parametres.getNbLinesWithErrorsToStop() >= 0
							&& errors.size() > parametres.getNbLinesWithErrorsToStop()) {
						throw new CsvErrorsExceededException(errors);
					}
				}
			} while (objet != null || erreurValidation);
		} finally {
			closeLecteurCourant();
		}
		return errors;
	}

	/**
	 * Scanne une class pour la gérer dans le moteur.
	 * 
	 * @param clazz
	 *            classe à scanner.
	 */
	protected void scannerClass(Class<?> clazz) {
		CsvFile csvFile = clazz.getAnnotation(CsvFile.class);
		if (csvFile == null) {
			throw new MoteurCsvException("Annotation CsvFile non présente sur la classe " + clazz.getSimpleName());
		}
		if (mapClasses.get(clazz) != null) {
			return;
		}
		CsvClass csvClass = new CsvClass(csvFile.separator(), clazz);
		for (Field field : clazz.getDeclaredFields()) {
			CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
			CsvValidation csvValidation = field.getAnnotation(CsvValidation.class);
			CsvValidations csvValidations = field.getAnnotation(CsvValidations.class);
			if (csvColumn != null) {
				csvClass.setCsvField(csvColumn.value(), new CsvField(csvColumn, csvValidations, csvValidation, field));
				csvClass.putOrder(csvColumn.value(), csvColumn.order());
			}
		}
		mapClasses.put(clazz, csvClass);
	}

	/**
	 * Ecrit une ligne dans le ficheir CSV.
	 * 
	 * @param <Objet>
	 *            Objet associé au CSV.
	 * @param csvWriter
	 *            writer à utiliser.
	 * @param nomChamps
	 *            liste des champs représentant l'entête du CSV.
	 * @param csvClass
	 *            classe associée au fichier CSV.
	 * @param objet
	 *            objet à écrire dans le CSV.
	 * @throws IllegalAccessException
	 *             ne doit pas arriver.
	 */
	private <Objet> void writeLigne(AbstractCsvWriter csvWriter, List<String> nomChamps, CsvClass csvClass, Objet objet)
			throws IllegalAccessException {
		List<String> champs = new ArrayList<String>();
		for (String nomChamp : nomChamps) {
			CsvField csvField = csvClass.getCsvField(nomChamp);
			csvField.getField().setAccessible(true);
			Object valeur = csvField.getField().get(objet);
			csvField.getField().setAccessible(false);
			if (valeur != null) {
				champs.add(csvField.getAdapterCsv().toString(valeur));
			} else {
				champs.add(null);
			}
		}
		csvWriter.writeLine(champs);
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
			final CsvClass csvClass = mapClasses.get(clazz);
			AbstractCsvWriter csvWriter =
					factory.createWriterCsv(writer, csvClass.getSeparatorWithoutEscape(), parametres.hasAddQuoteCar());
			try {
				List<String> nomChamps = new ArrayList<String>();
				for (String champ : csvClass.getColumnNames()) {
					nomChamps.add(champ);
				}
				Collections.sort(nomChamps, new Comparator<String>() {
					public int compare(String o1, String o2) {
						int thisVal = csvClass.getOrder(o1);
						int anotherVal = csvClass.getOrder(o2);
						return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
					}
				});
				csvWriter.writeLine(nomChamps);
				for (Objet objet : objets) {
					writeLigne(csvWriter, nomChamps, csvClass, objet);
				}
			} finally {
				csvWriter.close();
			}
		} catch (Exception exception) {
			throw new MoteurCsvException(exception);
		}
	}
}
