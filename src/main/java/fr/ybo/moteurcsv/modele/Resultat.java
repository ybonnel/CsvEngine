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
package fr.ybo.moteurcsv.modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant le résultat du moteur.<br>
 * Contient :
 * <ul>
 * <li>La liste de objets transormés.</li>
 * <li>La liste des erreurs rencontrées.</li>
 * </ul>
 * 
 * @param <T>
 *            Objet contenu dans le résultat.
 * 
 */
public class Resultat<T> {

	/**
	 * Les objets transformés.
	 */
	private List<T> objets;

	/**
	 * Les erreurs rencontrées.
	 */
	private List<Erreur> erreurs;

	/**
	 * @return Les objets transformés.
	 */
	public List<T> getObjets() {
		if (objets == null) {
			objets = new ArrayList<T>();
		}
		return objets;
	}

	/**
	 * @return les erreurs rencontrées.
	 */
	public List<Erreur> getErreurs() {
		if (erreurs == null) {
			erreurs = new ArrayList<Erreur>();
		}
		return erreurs;
	}
}
