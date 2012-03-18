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

import java.util.Map;

import fr.ybo.moteurcsv.exception.InvalideParamException;
import fr.ybo.moteurcsv.validator.ValidateException;

/**
 * Interface à implémenter pour tout les adapter CSV.
 * 
 * @author ybonnel
 * 
 * @param <Objet>
 *            Objet à adapter en CSV (associé à une colonne).
 */
public abstract class AdapterCsv<Objet> {

	/**
	 * Méthode appelée après la contruction de l'adapter.<br/>
	 * Méthode à surcharger si on souhaite utiliser des paramètres.
	 * 
	 * @param params
	 *            paramètres.
	 * @throws InvalideParamException
	 *             si les paramètres ne sont pas corrects.
	 */
	public void addParams(Map<String, String> params) throws InvalideParamException {
	}

	/**
	 * Transforme une chaine en Objet.
	 * 
	 * @param chaine
	 *            la chaine à transformer.
	 * @return l'objet tranformé.
	 * @throws ValidateException
	 *             peut être envoyée si la chaine n'a pas le bon format.
	 */
	public abstract Objet parse(String chaine) throws ValidateException;

	/**
	 * Transforme un objet en chaine.
	 * 
	 * @param objet
	 *            objet à tranformer.
	 * @return la chaine obtenu.
	 */
	public abstract String toString(Objet objet);
}
