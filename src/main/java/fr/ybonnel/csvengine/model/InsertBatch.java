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

import java.util.List;

/**
 * Handler for batch read on a CSV File.<br/<br/>
 * <i><u>French :</i> Traitement à effectuer pendant la lecture d'un fichier CSV par lot.
 *
 * @see fr.ybonnel.csvengine.CsvEngine#parseFileAndHandleBatch(java.io.Reader, Class, InsertBatch, int)
 *
 * @author ybonnel
 *
 * @param <T>
 *            class to handle.
 *
 * @author ybonnel
 */
public interface InsertBatch<T> {

    /**
     * Handler for each batch.
     * @param objects a batch of objects.
     */
    void handleBatch(List<T> objects);
}
