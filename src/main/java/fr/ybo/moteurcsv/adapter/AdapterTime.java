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

public class AdapterTime implements AdapterCsv<Integer> {

	private static final int MINUTES_BY_HOUR = 60;

	public Integer parse(String chaine) {
		if (chaine == null) {
			return null;
		}
		String[] champs = chaine.split(":");
		if (champs.length < 2) {
			return null;
		}
		return Integer.parseInt(champs[0]) * MINUTES_BY_HOUR + Integer.parseInt(champs[1]);
	}

	public String toString(Integer objet) {
		if (objet == null) {
			return null;
		}
		StringBuilder retour = new StringBuilder();
		int heures = objet / MINUTES_BY_HOUR;
		int minutes = objet - heures * MINUTES_BY_HOUR;
		if (heures < 10) {
			retour.append('0');
		}
		retour.append(heures);
		retour.append(':');
		if (minutes < 10) {
			retour.append('0');
		}
		retour.append(minutes);
		retour.append(":00");
		return retour.toString();
	}
}
