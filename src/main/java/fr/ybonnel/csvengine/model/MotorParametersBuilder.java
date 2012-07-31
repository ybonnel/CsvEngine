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
package fr.ybonnel.csvengine.model;

/**
 * Builder for the class {@link EngineParameters}.<br/><br/>
 * <i><u>French :</i> Builder pour la classe {@link EngineParameters}.
 *
 * @author ybonnel
 */
public class MotorParametersBuilder {

    /**
     * Instance of EngineParameters to construct.
     */
    private final EngineParameters parameters = new EngineParameters();

    /**
     * Protected constructor. To construct a builder, use {@link EngineParameters#createBuilder()}.
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
     * Construct the instance of {@link EngineParameters}.
     *
     * @return the constructed instance.
     */
    public EngineParameters build() {
        return parameters;
    }

}
