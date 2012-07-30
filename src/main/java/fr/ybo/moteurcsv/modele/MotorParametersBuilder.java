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

/**
 * Builder for the class {@link MotorParameters}.<br/><br/>
 * <u><i>French :</i></u> Builder pour la classe {@link MotorParameters}.
 *
 * @author ybonnel
 */
public class MotorParametersBuilder {

    /**
     * Instance of MotorParameters to construct.
     */
    private final MotorParameters parameters = new MotorParameters();

    /**
     * Protected constructor. To construct a builder, use {@link MotorParameters#createBuilder()}.
     */
    protected MotorParametersBuilder() {
    }

    /**
     * Activate or not the validation.
     *
     * @param validation true if the validation is active.
     * @return the builder.
     */
    public MotorParametersBuilder setValidation(boolean validation) {
        parameters.setValidation(validation);
        return this;
    }

    /**
     * Number of lines in error before stop.
     *
     * @param nbLinesWithErrorsToStop Number of lines in error before stop.
     * @return the builder.
     */
    public MotorParametersBuilder setNbLinesWithErrorsToStop(int nbLinesWithErrorsToStop) {
        parameters.setNbLinesWithErrorsToStop(nbLinesWithErrorsToStop);
        return this;
    }

    /**
     * Activate of not the add of quote for a field.
     *
     * @param addQuoteCar
     *            true to activate the add of quote for a field.
     * @return the builder.
     */
    public MotorParametersBuilder setAddQuoteCar(boolean addQuoteCar) {
        parameters.setAddQuoteCar(addQuoteCar);
        return this;
    }

    /**
     * Construct the instance of {@link MotorParameters}.
     *
     * @return the constructed instance.
     */
    public MotorParameters build() {
        return parameters;
    }

}
