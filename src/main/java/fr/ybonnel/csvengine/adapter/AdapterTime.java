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

/**
 * Adapter for Time in format "HH:MI". Return an Integer represents number of minutes after midnight
 * (very specific adapter).<br/><br/>
 * <i><u>French :</i> Adapteur pour les heures au format HH:MI. Retourne un integrer représentant
 * le nombre de minutes passées de minuit.
 *
 * @author ybonnel
 */
public class AdapterTime extends AdapterCsv<Integer> {

    /**
     * Ten.
     */
    private static final int TEN = 10;
    /**
     * Number of minutes in an hour.
     */
    private static final int MINUTES_BY_HOUR = 60;

    /**
     * Transform a string in format HH:MI into an Integer represents number of minutes after midnight.
     *
     * @param string string to transform.
     * @return Integer of time resulting.
     * @see AdapterCsv#parse(java.lang.String)
     */
    public Integer parse(String string) {
        String[] champs = string.split(":");
        if (champs.length < 2) {
            return null;
        }
        return Integer.parseInt(champs[0]) * MINUTES_BY_HOUR + Integer.parseInt(champs[1]);
    }

    /**
     * Transform an Integer represents number of minutes after midnight info a String in format HH:MI.
     * @param object Integer of time to transform.
     * @return string resulting.
     *
     * @see AdapterCsv#toString(Object)
     */
    public String toString(Integer object) {
        StringBuilder retour = new StringBuilder();
        int heures = object / MINUTES_BY_HOUR;
        int minutes = object - heures * MINUTES_BY_HOUR;
        if (heures < TEN) {
            retour.append('0');
        }
        retour.append(heures);
        retour.append(':');
        if (minutes < TEN) {
            retour.append('0');
        }
        retour.append(minutes);
        retour.append(":00");
        return retour.toString();
    }
}
