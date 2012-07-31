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
package fr.ybonnel.csvengine.validator;

import java.util.Map;

import fr.ybonnel.csvengine.exception.InvalidParamException;

/**
 * Validator use to validate a field on its size (min and max). <br/>
 * Parameters :
 * <ul>
 * <li>{@link ValidatorSize#PARAM_MIN_SIZE} contains the minimum size of the field.</li>
 * <li>{@link ValidatorSize#PARAM_MAX_SIZE} contains the maximum size of the field.</li>
 * </ul>
 * At least one of this parameters must be passed<br/>
 * The parameters must be passed with
 * {@link fr.ybonnel.csvengine.annotation.CsvValidation#params()}.<br/><br/>
 * <p/>
 * <i><u>French :</i>Validateur permettant de valider un champ sur sa taille (min et max). <br/>
 * Paramètre :
 * <ul>
 * <li>{@link ValidatorSize#PARAM_MIN_SIZE} contient la taille minimum du
 * champ.</li>
 * <li>{@link ValidatorSize#PARAM_MAX_SIZE} contient la taille minimum du
 * champ.</li>
 * </ul>
 * Au moins un des deux paramètres doit être fournit<br/>
 * Les paramètres sont à fournir via
 * {@link fr.ybonnel.csvengine.annotation.CsvValidation#params()}.
 */
public class ValidatorSize extends ValidatorCsv {

    /**
     * Parameter with the minimum size.
     */
    public static final String PARAM_MIN_SIZE = "minSize";

    /**
     * Parameter with the maximum size.
     */
    public static final String PARAM_MAX_SIZE = "maxSize";

    /**
     * Minimum size of the field.
     */
    private int tailleMin = -1;
    /**
     * Maximum size of the field.
     */
    private int tailleMax = Integer.MAX_VALUE;

    /*
      * (non-Javadoc)
      *
      * @see ValidatorCsv#addParams(java.util.Map)
      */
    @Override
    public void addParams(Map<String, String> params) throws InvalidParamException {
        super.addParams(params);
        String minSizeString = params.get(PARAM_MIN_SIZE);
        String maxSizeString = params.get(PARAM_MAX_SIZE);
        if (minSizeString == null && maxSizeString == null) {
            throw new InvalidParamException("At least one of the parameters \"" + PARAM_MIN_SIZE + "\" or \""
                    + PARAM_MAX_SIZE + "\" must be passed");
        }
        if (minSizeString != null) {
            try {
                tailleMin = Integer.parseInt(minSizeString);
            } catch (NumberFormatException exception) {
                throw new InvalidParamException("The parameter \"" + PARAM_MIN_SIZE + "\" isn't in good format",
                        exception);
            }
        }
        if (maxSizeString != null) {
            try {
                tailleMax = Integer.parseInt(maxSizeString);
            } catch (NumberFormatException exception) {
                throw new InvalidParamException("The parameter \"" + PARAM_MAX_SIZE + "\" isn't in good format",
                        exception);
            }
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see fr.ybo.csvengine.validator.ValidatorCsv#validate(java.lang.String)
      */
    @Override
    public void validate(String field) throws ValidateException {
        if (field.length() < tailleMin || field.length() > tailleMax) {
            throw new ValidateException("The value " + field + " haven't the good size (between " + tailleMin
                    + " and " + tailleMax + ")");
        }
    }

}
