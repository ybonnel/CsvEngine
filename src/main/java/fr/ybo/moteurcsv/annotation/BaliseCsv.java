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
package fr.ybo.moteurcsv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.ybo.moteurcsv.adapter.AdapterCsv;
import fr.ybo.moteurcsv.adapter.AdapterString;

/**
 * Annotation utilisée pour représenté une colonne csv.
 * 
 * @author ybonnel
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BaliseCsv {

	/**
	 * adapter à utiliser pour transformer l'objet en chaine et inversement.
	 */
	Class<? extends AdapterCsv<?>> adapter() default AdapterString.class;

	/**
	 * nom de la colonne CSV.
	 */
	String value();

	/**
	 * Ordre de la colonne (permet d'assurer que les colonnes soient toujours
	 * écrite dans un ordre défini).
	 */
	int ordre() default 0;

	/**
	 * Permet de décrire si le champ est obligatoire (false par défaut).
	 */
	boolean obligatoire() default false;
}
