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
package fr.ybonnel.csvengine.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import fr.ybonnel.csvengine.exception.InvalidParamException;
import fr.ybonnel.csvengine.validator.ValidateException;

/**
 * Adapter for java.util.Date<br/>
 * <br/>
 * Parameter : {@link AdapterDate#PARAM_FORMAT} contain the format of the date.<br/>
 * The parameter have to be passed with {@link fr.ybonnel.csvengine.annotation.CsvColumn#params()}.
 * <br/><br/>
 * <p/>
 * <i><u>French :</i> Adapteur pour les dates.<br/>
 * <br/>
 * Paramètre : {@link AdapterDate#PARAM_FORMAT} contient le format de la date.<br/>
 * Le paramètre format est à fournir via
 * {@link fr.ybonnel.csvengine.annotation.CsvColumn#params()}.
 */
public class AdapterDate extends AdapterCsv<Date> {

    /**
     * Parameter for the Date Format.
     */
    public static final String PARAM_FORMAT = "format";

    /**
     * SimpleDateFormat used for parsed and format dates.
     */
    private SimpleDateFormat format;

    /*
      * (non-Javadoc)
      *
      * @see AdapterCsv#addParams(java.util.Map)
      */
    @Override
    public void addParams(Map<String, String> params) throws InvalidParamException {
        super.addParams(params);
        if (!params.containsKey(PARAM_FORMAT)) {
            throw new InvalidParamException("The parameter \"" + PARAM_FORMAT + "\" is mandatory");
        }
        try {
            format = new SimpleDateFormat(params.get(PARAM_FORMAT));
        } catch (IllegalArgumentException exception) {
            throw new InvalidParamException("The parameter \"" + PARAM_FORMAT + "\" haven't the good format : "
                    + params.get(PARAM_FORMAT), exception);
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see AdapterCsv#parse(java.lang.String)
      */
    @Override
    public Date parse(String string) throws ValidateException {
        try {
            return format.parse(string);
        } catch (ParseException e) {
            throw new ValidateException(
                    "A date haven't the good format, (expected format : " + format.toPattern() + ")", e);
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see AdapterCsv#toString(java.lang.Object)
      */
    @Override
    public String toString(Date object) {
        return format.format(object);
    }

}
