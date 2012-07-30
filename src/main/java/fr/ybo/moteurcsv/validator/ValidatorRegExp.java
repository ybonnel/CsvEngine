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
package fr.ybo.moteurcsv.validator;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import fr.ybo.moteurcsv.exception.InvalidParamException;

/**
 * Validator use to validate a field with a regular expression<br/>
 * Parameter : {@link ValidatorRegExp#PARAM_PATTERN} contains the regular expression ({@link Pattern}).<br/>
 * The parameter must be passed with
 * {@link fr.ybo.moteurcsv.annotation.CsvValidation#params()}.<br/><br/>
 *
 * <u><i>French :</i></u> Validateur permettant de valider un champ à partir d'une expression
 * régulière. <br/>
 * Paramètre : {@link ValidatorRegExp#PARAM_PATTERN} contient l'expression
 * régulière ({@link Pattern}).<br/>
 * Le paramètre est à fournir via
 * {@link fr.ybo.moteurcsv.annotation.CsvValidation#params()}.
 */
public class ValidatorRegExp extends ValidatorCsv {

	/**
	 * Parameter which contains the regular expression.
	 */
	public static final String PARAM_PATTERN = "pattern";

	/**
	 * Compiled pattern.
	 */
	private Pattern pattern;

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ybo.moteurcsv.validator.ValidatorCsv#addParams(java.util.Map)
	 */
	@Override
	public void addParams(Map<String, String> params) throws InvalidParamException {
		super.addParams(params);
		if (!params.containsKey(PARAM_PATTERN)) {
			throw new InvalidParamException("The parameter \"" + PARAM_PATTERN + "\" is mandatory");
		}
		try {
			pattern = Pattern.compile(params.get(PARAM_PATTERN));
		} catch (PatternSyntaxException exception) {
			throw new InvalidParamException("The parameter \"" + PARAM_PATTERN + "\" hasn't the good format", exception);
		}
	}

	/* (non-Javadoc)
	 * @see fr.ybo.moteurcsv.validator.ValidatorCsv#validate(java.lang.String)
	 */
	@Override
	public void validate(String field) throws ValidateException {
		if (!pattern.matcher(field).matches()) {
			throw new ValidateException("The value " + field + " doesn't correspond to the pattern " + pattern.pattern());
		}
	}

}
