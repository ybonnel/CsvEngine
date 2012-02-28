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
 * Classe représentant une erreur.
 * 
 * @author ybonnel
 * 
 */
public class Erreur {

	/**
	 * Ligne du CSV.
	 */
	private String ligneCsv;

	/**
	 * @return la ligne du CSV posant problème.
	 */
	public String getLigneCsv() {
		return ligneCsv;
	}

	/**
	 * @param ligneCsv
	 *            ligne du CSV posant problème.
	 */
	public void setLigneCsv(String ligneCsv) {
		this.ligneCsv = ligneCsv;
	}

	/**
	 * Liste des messages d'erreurs.
	 */
	private List<String> messages;

	/**
	 * @return les messages d'erreur.
	 */
	public List<String> getMessages() {
		if (messages == null) {
			messages = new ArrayList<String>();
		}
		return messages;
	}
}
