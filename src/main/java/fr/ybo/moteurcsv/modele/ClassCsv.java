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
package fr.ybo.moteurcsv.modele;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import fr.ybo.moteurcsv.exception.MoteurCsvException;

/**
 * Répésente une classe associé à un fichier CSV.
 * 
 * @author ybonnel
 * 
 */
public class ClassCsv {

	/**
	 * Séparateur.
	 */
	private final String separateur;

	/**
	 * La classe.
	 */
	private final Class<?> clazz;

	/**
	 * Le constructeur.
	 */
	private Constructor<?> contructeur;

	/**
	 * Map des attributs de la classe.
	 */
	private final Map<String, ChampCsv> mapOfFields = new HashMap<String, ChampCsv>(5);

	/**
	 * Map des ordres.
	 */
	private final Map<String, Integer> ordres = new HashMap<String, Integer>();

	/**
	 * Constructeur.
	 * 
	 * @param separateur
	 *            séparateur.
	 * @param clazz
	 *            la classe.
	 */
	public ClassCsv(String separateur, Class<?> clazz) {
		this.separateur = separateur;
		this.clazz = clazz;
		try {
			contructeur = clazz.getDeclaredConstructor((Class<?>[]) null);
			contructeur.setAccessible(true);
		} catch (Exception e) {
			throw new MoteurCsvException("Erreur a la récupération du constructeur de " + clazz.getSimpleName()
					+ ", il doit manquer le constructeur sans paramètre", e);
		}
	}

	/**
	 * Retourne le champs associé au nom de colonne.
	 * 
	 * @param nomCsv
	 *            nom de la colonne.
	 * @return le champ.
	 */
	public ChampCsv getChampCsv(String nomCsv) {
		return mapOfFields.get(nomCsv);
	}

	/**
	 * @return la liste des colonnes CSV.
	 */
	public Iterable<String> getNomChamps() {
		return mapOfFields.keySet();
	}

	/**
	 * @return la classe.
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * @return le constructeur.
	 */
	public Constructor<?> getContructeur() {
		return contructeur;
	}
	
	/**
	 * Séparateur sans escape.
	 */
	private String separateurWithoutEscape = null;

	/**
	 * @return Séparateur sans escape.
	 */
	public char getSeparateurWithoutEscape() {
		if (separateurWithoutEscape == null) {
			separateurWithoutEscape = separateur.replaceAll("\\\\", "");
		}
		if (separateurWithoutEscape.length() != 1) {
			throw new MoteurCsvException("Le séparateur " + separateurWithoutEscape + " contient plus d'1 caractère.");
		}
		return separateurWithoutEscape.charAt(0);
		
	}

	/**
	 * @param nomCsv
	 *            nom du champ.
	 * @param champCsv
	 *            champ CSV.
	 */
	public void setChampCsv(String nomCsv, ChampCsv champCsv) {
		mapOfFields.put(nomCsv, champCsv);
	}

	/**
	 * Ajout de l'ordre.
	 * 
	 * @param nomCsv
	 *            nom du champ.
	 * @param ordre
	 *            ordre.
	 */
	public void putOrdre(String nomCsv, int ordre) {
		ordres.put(nomCsv, ordre);
	}

	/**
	 * Retourne l'ordre d'un champ.
	 * 
	 * @param nomCsv
	 *            nom du champ.
	 * @return ordre associé.
	 */
	public int getOrdre(String nomCsv) {
		return ordres.get(nomCsv);
	}

}
