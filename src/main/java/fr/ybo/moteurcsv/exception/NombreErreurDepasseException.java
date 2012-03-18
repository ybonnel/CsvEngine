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
package fr.ybo.moteurcsv.exception;

import java.util.List;

import fr.ybo.moteurcsv.modele.Erreur;

/**
 * Cette exception est déclenchée quand le nombre d'erreurs atteint dépasse le
 * nombre autorisé.
 * 
 */
public class NombreErreurDepasseException extends Exception {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Liste des erreurs rencontrées.
	 */
	private List<Erreur> erreurs;

	/**
	 * Constructeur.
	 * 
	 * @param erreurs
	 *            les erreurs rencontrées.
	 */
	public NombreErreurDepasseException(List<Erreur> erreurs) {
		super();
		this.erreurs = erreurs;
	}

	/**
	 * @return les erreurs rencontrées.
	 */
	public List<Erreur> getErreurs() {
		return erreurs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder();
		for (Erreur erreur : getErreurs()) {
			for (String message : erreur.getMessages()) {
				builder.append(message);
				builder.append('\n');
			}
			if (builder.length() > 4000) {
				break;
			}
		}
		return builder.toString();
	}

}
