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

import fr.ybo.moteurcsv.exception.InvalideParamException;

/**
 * Validateur permettant de valider un champ à partir d'une expression
 * régulière. <br/>
 * Paramètre : {@link ValidatorRegExp#PARAM_PATTERN} contient l'expression
 * régulière ({@link Pattern}).<br/>
 * Le paramètre format est à fournir via
 * {@link fr.ybo.moteurcsv.annotation.BaliseCsv#params()}.
 */
public class ValidatorRegExp extends ValidatorCsv {

	/**
	 * Paramètre contenant l'expression régulière.
	 */
	public static final String PARAM_PATTERN = "pattern";

	/**
	 * Pattern compilé.
	 */
	private Pattern pattern;

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ybo.moteurcsv.validator.ValidatorCsv#addParams(java.util.Map)
	 */
	@Override
	public void addParams(Map<String, String> params) throws InvalideParamException {
		super.addParams(params);
		if (!params.containsKey(PARAM_PATTERN)) {
			throw new InvalideParamException("Le paramètre \"" + PARAM_PATTERN + "\" est obligatoire");
		}
		try {
			pattern = Pattern.compile(params.get(PARAM_PATTERN));
		} catch (PatternSyntaxException exception) {
			throw new InvalideParamException("Le paramètre \"" + PARAM_PATTERN + "\" n'a pas le bon format", exception);
		}
	}

	/* (non-Javadoc)
	 * @see fr.ybo.moteurcsv.validator.ValidatorCsv#validate(java.lang.String)
	 */
	@Override
	public void validate(String champ) throws ValidateException {
		if (!pattern.matcher(champ).matches()) {
			throw new ValidateException("La valeur " + champ + " ne correspond pas au pattern " + pattern.pattern());
		}
	}

}
