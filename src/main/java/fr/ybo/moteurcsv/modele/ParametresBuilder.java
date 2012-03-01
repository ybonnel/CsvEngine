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
 * Builder pour la classe {@link Parametres}.
 * 
 * @author ybonnel
 * 
 */
public class ParametresBuilder {

	/**
	 * Instance de Parametres à construire.
	 */
	private final Parametres parametres = new Parametres();

	/**
	 * Constructeur protégé. Pour construire un builder il faut appeler
	 * {@link Parametres#createBuilder()}.
	 */
	protected ParametresBuilder() {
	}

	/**
	 * Permet d'activer ou non la validation du moteur.
	 * 
	 * @param validation
	 *            true si la validation doit être activée.
	 * @return le builder.
	 */
	public ParametresBuilder setValidation(boolean validation) {
		parametres.setValidation(validation);
		return this;
	}

	/**
	 * Permet de régler le nombre de lignes en erreur que peux rencontrer le
	 * moteur avant de s'arrêter.
	 * 
	 * @param nbLinesWithErrorsToStop
	 *            nombre de lignes en erreur possible.
	 * @return le builder.
	 */
	public ParametresBuilder setNbLinesWithErrorsToStop(int nbLinesWithErrorsToStop) {
		parametres.setNbLinesWithErrorsToStop(nbLinesWithErrorsToStop);
		return this;
	}

	/**
	 * Construit l'instance de {@link Parametres}.
	 * 
	 * @return
	 */
	public Parametres build() {
		return parametres;
	}

}
