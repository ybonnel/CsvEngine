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
package fr.ybo.moteurcsv.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import fr.ybo.moteurcsv.exception.InvalideParamException;
import fr.ybo.moteurcsv.validator.ValidateException;

/**
 * Adapteur pour les dates.<br/>
 * <br/>
 * Paramètre : {@link AdapterDate#PARAM_FORMAT} contient le format de la date.<br/>
 * Le paramètre format est à fournir via
 * {@link fr.ybo.moteurcsv.annotation.BaliseCsv#params()}.
 */
public class AdapterDate extends AdapterCsv<Date> {

	/**
	 * Paramètre format.
	 */
	public static final String PARAM_FORMAT = "format";

	/**
	 * SimpleDateFormat utilisé pour parser et formater les dates.
	 */
	private SimpleDateFormat format;

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ybo.moteurcsv.adapter.AdapterCsv#addParams(java.util.Map)
	 */
	@Override
	public void addParams(Map<String, String> params) throws InvalideParamException {
		super.addParams(params);
		if (!params.containsKey(PARAM_FORMAT)) {
			throw new InvalideParamException("Le paramètre \"" + PARAM_FORMAT + "\" est obligatoire");
		}
		try {
			format = new SimpleDateFormat(params.get(PARAM_FORMAT));
		} catch (IllegalArgumentException exception) {
			throw new InvalideParamException("Le paramètre \"" + PARAM_FORMAT + "\" n'a pas le bon format : "
					+ params.get(PARAM_FORMAT), exception);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ybo.moteurcsv.adapter.AdapterCsv#parse(java.lang.String)
	 */
	@Override
	public Date parse(String chaine) throws ValidateException {
		try {
			return format.parse(chaine);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			throw new ValidateException(
					"Une date n'a pas le bon format, (format attendu : " + format.toPattern() + ")", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ybo.moteurcsv.adapter.AdapterCsv#toString(java.lang.Object)
	 */
	@Override
	public String toString(Date objet) {
		return format.format(objet);
	}

}
