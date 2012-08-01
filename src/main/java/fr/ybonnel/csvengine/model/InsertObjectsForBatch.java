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

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Internal class of the CsvEngine.</b><br/>
 * This class is use for the method
 * {@link fr.ybonnel.csvengine.CsvEngine#parseFileAndHandleBatch(java.io.Reader, Class, InsertBatch, int)}.
 *
 * @param <T> class of objects.
 *
 * @author ybonnel
 */
public class InsertObjectsForBatch<T> implements InsertObject<T> {

    /**
     * Handler to transmit each batch of objects.
     */
    private InsertBatch<T> handler;

    /**
     * Size of a batch.
     */
    private int batchSize;

    /**
     * The current list of objects.
     */
    private List<T> currentListOfObjects = new ArrayList<T>();

    /**
     *
     * @return the current list of object.
     */
    public List<T> getCurrentListOfObjects() {
        return currentListOfObjects;
    }

    /**
     * Constructor.
     * @param handler handler to transmit each batch of objects.
     * @param batchSize size of a batch.
     */
    public InsertObjectsForBatch(InsertBatch<T> handler, int batchSize) {
        this.handler = handler;
        this.batchSize = batchSize;
    }

    /**
     * Add the object into the current list of objects
     * and transmit the list if necessary to handler.
     * @param object one object.
     */
    public void insertObject(T object) {
        currentListOfObjects.add(object);
        if (currentListOfObjects.size() >= batchSize) {
            handler.handleBatch(currentListOfObjects);
            currentListOfObjects = new ArrayList<T>();
        }
    }
}
