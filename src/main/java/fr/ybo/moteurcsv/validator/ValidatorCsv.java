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

/**
 * Interface à implémenter pour ajouter un nouveau Validateur de champs CSV.
 * 
 * @author ybonnel
 * 
 */
public abstract class ValidatorCsv {

	/**
	 * Méthode à implémenter pour la validation du champ.
	 * 
	 * @param champ
	 *            champ à valider.
	 * @throws ValidateException
	 *             en cas d'erreur de validation.
	 */
	public abstract void validate(String champ) throws ValidateException;
}
