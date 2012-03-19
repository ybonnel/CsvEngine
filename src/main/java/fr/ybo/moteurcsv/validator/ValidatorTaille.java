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

import fr.ybo.moteurcsv.exception.InvalideParamException;

/**
 * Validateur permettant de valider un champ sur sa taille (min et max). <br/>
 * Paramètre :
 * <ul>
 * <li>{@link ValidatorTaille#PARAM_TAILLE_MIN} contient la taille minimum du
 * champ.</li>
 * <li>{@link ValidatorTaille#PARAM_TAILLE_MAX} contient la taille minimum du
 * champ.</li>
 * </ul>
 * Au moins un des deux paramètres doit être fournit<br/>
 * Le paramètre format est à fournir via
 * {@link fr.ybo.moteurcsv.annotation.Validation#params()}.
 */
public class ValidatorTaille extends ValidatorCsv {

	/**
	 * Paramètre contenant la taille min.
	 */
	public static final String PARAM_TAILLE_MIN = "tailleMin";

	/**
	 * Paramètre contenant la taille max.
	 */
	public static final String PARAM_TAILLE_MAX = "tailleMax";

	/**
	 * Taille minimum du champ.
	 */
	private int tailleMin = -1;
	/**
	 * Taille maximum du champ.
	 */
	private int tailleMax = Integer.MAX_VALUE;

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ybo.moteurcsv.validator.ValidatorCsv#addParams(java.util.Map)
	 */
	@Override
	public void addParams(Map<String, String> params) throws InvalideParamException {
		super.addParams(params);
		String tailleMinChaine = params.get(PARAM_TAILLE_MIN);
		String tailleMaxChaine = params.get(PARAM_TAILLE_MAX);
		if (tailleMinChaine == null && tailleMaxChaine == null) {
			throw new InvalideParamException("Au moins un des deux paramètres \"" + PARAM_TAILLE_MIN + "\" ou \""
					+ PARAM_TAILLE_MAX + "\" doit être fournit");
		}
		if (tailleMinChaine != null) {
			try {
				tailleMin = Integer.parseInt(tailleMinChaine);
			} catch (NumberFormatException exception) {
				throw new InvalideParamException("Le paramètre \"" + PARAM_TAILLE_MIN + "\" n'a pas le bon format",
						exception);
			}
		}
		if (tailleMaxChaine != null) {
			try {
				tailleMax = Integer.parseInt(tailleMaxChaine);
			} catch (NumberFormatException exception) {
				throw new InvalideParamException("Le paramètre \"" + PARAM_TAILLE_MAX + "\" n'a pas le bon format",
						exception);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ybo.moteurcsv.validator.ValidatorCsv#validate(java.lang.String)
	 */
	@Override
	public void validate(String champ) throws ValidateException {
		if (champ.length() < tailleMin || champ.length() > tailleMax) {
			throw new ValidateException("La valeur " + champ + " n'a pas la taille requise (entre " + tailleMin
					+ " et " + tailleMax + ")");
		}
	}

}
