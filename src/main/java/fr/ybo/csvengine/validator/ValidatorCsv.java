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
package fr.ybo.csvengine.validator;

import java.util.Map;

import fr.ybo.csvengine.exception.InvalidParamException;

/**
 * Class to extend for a new Validator of CSV Fields.<br/><br/>
 * <i><u>French :</i> Classe à étendre pour ajouter un nouveau Validateur de champs CSV.
 * 
 * @author ybonnel
 * 
 */
public abstract class ValidatorCsv {

	/**
     * Method call after the construction of validator<br/>
     * Method to override if you want use parameters.
	 * 
	 * @param params
	 *            parameters.
     * @throws fr.ybo.csvengine.exception.InvalidParamException if parameters are incorrect.
	 */
	public void addParams(Map<String, String> params) throws InvalidParamException {
	}

	/**
     * Method to implement to validate a field.
	 * 
	 * @param field
	 *            field to validate.
	 * @throws ValidateException
	 *             in case of validation error.
	 */
	public abstract void validate(String field) throws ValidateException;
}
