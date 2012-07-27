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
 * Annotation used on attribute of class which is a column of CSV file<br/><br/>
 * <u><i>French :</i></u> Annotation utilisée pour représenté une colonne csv.
 * 
 * @author ybonnel
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvColumn {

	/**
     * Adapter to use to transform the Object into String and reverse.
	 */
	Class<? extends AdapterCsv<?>> adapter() default AdapterString.class;

	/**
	 * Name of the CSV Column.
	 */
	String value();

	/**
     * Order of the column (useful to be sure that column are always write in a defined order).
	 */
	int order() default 0;

	/**
     * Describe if the column is mandatory (false by default).
	 */
	boolean mandatory() default false;

	/**
     * Parametes of the adapter, no parameters by default.
	 */
	CsvParam[] params() default { };
}
