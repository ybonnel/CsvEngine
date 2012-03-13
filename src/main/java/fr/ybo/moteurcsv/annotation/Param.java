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

/**
 * Annotation utilisée pour contenir un paramètre des Adapter et des Validator.
 * 
 * @author ybonnel
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Param {

	/**
	 * Nom du paramètre.
	 */
	String name();

	/**
	 * Valeur du paramètre.
	 */
	String value();

	/**
	 * Type du paramètre : String par défaut.
	 * 
	 * @return
	 */
	TypeParam type() default TypeParam.STRING;

	/**
	 * Représente le type d'un paramètre.
	 */
	public static enum TypeParam {
		/**
		 * {@link Integer}.
		 */
		INTEGER(Integer.class),
		/**
		 * {@link String}.
		 */
		STRING(String.class);

		/**
		 * Classe associée au type.
		 */
		private Class<?> clazz;

		/**
		 * Constructeur de l'énum.
		 * 
		 * @param clazz
		 *            classe associée au type.
		 */
		private TypeParam(Class<?> clazz) {
			this.clazz = clazz;
		}

		/**
		 * @return classe associée au type.
		 */
		public Class<?> getClazz() {
			return clazz;
		}
	}
}
