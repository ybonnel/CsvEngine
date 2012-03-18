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

/**
 * Classe contenant tous les paramètres du moteur.
 * 
 * @author ybonnel
 * 
 */
public class ParametresMoteur {

	/**
	 * Permet d'activer ou non la validation.
	 */
	private boolean validation = true;

	/**
	 * Permet de régler le nombre de lignes en erreurs avant de s'arrêter.
	 */
	private int nbLinesWithErrorsToStop = 0;

	/**
	 * Permet de désactiver l'ajout de guillemets pour encadrer les champs.
	 */
	private boolean addQuoteCar = true;

	/**
	 * Création d'un builder.
	 * 
	 * @return le builder.
	 */
	public static ParametresMoteurBuilder createBuilder() {
		return new ParametresMoteurBuilder();
	}

	/**
	 * @return validation active?
	 */
	public boolean hasValidation() {
		return validation;
	}

	/**
	 * Permet d'activer ou non la validation.<br/>
	 * Par défaut la validation est active.
	 * 
	 * @param validation
	 *            true si la validation doit être activée.
	 */
	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	/**
	 * @return nombre de lignes en erreurs avant de tout arrêter.
	 */
	public int getNbLinesWithErrorsToStop() {
		return nbLinesWithErrorsToStop;
	}

	/**
	 * Permet de régler le nombre de lignes en erreur avant d'arrêter le moteur.<br/>
	 * 0 par défaut.
	 * 
	 * @param nbLinesWithErrorsToStop
	 *            nombre de lignes en erreur avant d'arrêter.
	 */
	public void setNbLinesWithErrorsToStop(int nbLinesWithErrorsToStop) {
		this.nbLinesWithErrorsToStop = nbLinesWithErrorsToStop;
	}

	/**
	 * @return l'ajout de caractère de délimitation de champ activé?
	 */
	public boolean hasAddQuoteCar() {
		return addQuoteCar;
	}

	/**
	 * Permet d'activer ou non l'ajout de délimiteur de champs (guillemet).<br/>
	 * true par défaut.
	 * 
	 * @param addQuoteCar
	 *            true pour activer l'ajout de délimiteurs.
	 */
	public void setAddQuoteCar(boolean addQuoteCar) {
		this.addQuoteCar = addQuoteCar;
	}

}
