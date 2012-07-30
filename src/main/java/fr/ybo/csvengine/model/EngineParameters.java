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
package fr.ybo.csvengine.model;

/**
 * Contains all parameters of the Engine.<br/><br/>
 * <u><i>French :</i></u> Classe contenant tous les paramètres du moteur.
 * 
 * @author ybonnel
 * 
 */
public class EngineParameters {

	/**
     * Activate or not the validation.
	 */
	private boolean validation = true;

	/**
     * Number of lines in error before stop.
	 */
	private int nbLinesWithErrorsToStop = 0;

	/**
     * Activate of not the add of quote for a field.
	 */
	private boolean addQuoteCar = true;

	/**
     * Creation of builder.
	 * 
	 * @return the builder.
	 */
	public static MotorParametersBuilder createBuilder() {
		return new MotorParametersBuilder();
	}

	/**
	 * @return validation active?
	 */
	public boolean hasValidation() {
		return validation;
	}

	/**
	 * Activate or not the validation.<br/>
     * By default the validation is active.
	 * 
	 * @param validation
	 *            true if the validation is active.
	 */
	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	/**
	 * @return Number of lines in error before stop.
	 */
	public int getNbLinesWithErrorsToStop() {
		return nbLinesWithErrorsToStop;
	}

	/**
	 * Number of lines in error before stop.<br/>
	 * 0 by default.
	 * 
	 * @param nbLinesWithErrorsToStop
	 *            Number of lines in error before stop.
	 */
	public void setNbLinesWithErrorsToStop(int nbLinesWithErrorsToStop) {
		this.nbLinesWithErrorsToStop = nbLinesWithErrorsToStop;
	}

	/**
	 * @return Activate of not the add of quote for a field.
	 */
	public boolean hasAddQuoteCar() {
		return addQuoteCar;
	}

	/**
	 * Activate of not the add of quote for a field.<br/>
	 * true by default.
	 * 
	 * @param addQuoteCar
	 *            true to activate the add of quote for a field.
	 */
	public void setAddQuoteCar(boolean addQuoteCar) {
		this.addQuoteCar = addQuoteCar;
	}

}
