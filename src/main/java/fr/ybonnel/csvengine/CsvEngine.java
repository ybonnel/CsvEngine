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
package fr.ybonnel.csvengine;

import fr.ybonnel.csvengine.annotation.CsvColumn;
import fr.ybonnel.csvengine.annotation.CsvFile;
import fr.ybonnel.csvengine.annotation.CsvValidation;
import fr.ybonnel.csvengine.annotation.CsvValidations;
import fr.ybonnel.csvengine.exception.CsvEngineException;
import fr.ybonnel.csvengine.exception.CsvErrorsExceededException;
import fr.ybonnel.csvengine.exception.StopParseException;
import fr.ybonnel.csvengine.factory.AbstractCsvReader;
import fr.ybonnel.csvengine.factory.AbstractCsvWriter;
import fr.ybonnel.csvengine.factory.CsvManagerFactory;
import fr.ybonnel.csvengine.factory.DefaultCsvManagerFactory;
import fr.ybonnel.csvengine.model.CsvClass;
import fr.ybonnel.csvengine.model.CsvField;
import fr.ybonnel.csvengine.model.EngineParameters;
import fr.ybonnel.csvengine.model.Error;
import fr.ybonnel.csvengine.model.InsertBatch;
import fr.ybonnel.csvengine.model.InsertInList;
import fr.ybonnel.csvengine.model.InsertObject;
import fr.ybonnel.csvengine.model.InsertObjectsForBatch;
import fr.ybonnel.csvengine.model.Result;
import fr.ybonnel.csvengine.validator.ValidateException;
import fr.ybonnel.csvengine.validator.ValidationError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Engine to write and read CSV File.<br/>
 * Examples of use :
 * <ul>
 * <li>Construction of the engine :<br/>
 * {@code CsvEngine engine = new CsvEngine(CsvObject.class);}</li>
 * <li>Read a CSV File :<br/>
 * {@code List<CsvObject> objects = engine.parseInputStream(stream, CsvObject.class);}</li>
 * <li>Write a CSV File :<br/>
 * {@code engine.writeFile(new FileWriter(file), objects, CsvObject.class);}</li>
 * </ul><br/><br/>
 * <p/>
 * <i><u>French :</i> Moteur de lecture et écriture de fichier CSV.<br/>
 * Voici des exemple d'utilisation :
 * <ul>
 * <li>Construction du moteur :<br/>
 * {@code CsvEngine moteur = new CsvEngine(CsvObject.class);}</li>
 * <li>Lecture d'un fichier CSV :<br/>
 * {@code List<CsvObject> objets = moteur.parseInputStream(stream, CsvObject.class);}
 * </li>
 * <li>Ecriture d'un fichier CSV :<br/>
 * {@code moteur.writeFile(new FileWriter(file), objets, CsvObject.class);}</li>
 * </ul>
 *
 * @author ybonnel
 */
public class CsvEngine {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(CsvEngine.class.getSimpleName());

    /**
     * Map of managed classes.
     */
    private final Map<Class<?>, CsvClass> mapClasses = new HashMap<Class<?>, CsvClass>();

    /**
     * Current header.
     */
    private String[] currentHeader;

    /**
     * Current class.
     */
    private CsvClass currentCsvClass;

    /**
     * Factory which deliver CsvReader and CsvWriter.
     */
    private CsvManagerFactory factory;

    /**
     * This method can be call to use an other implementation than open-csv for read and write of CSV File.
     *
     * @param factory factory which deliver CsvReader and CsvWriter.
     */
    public void setFactory(CsvManagerFactory factory) {
        this.factory = factory;
    }

    /**
     * Parameters of Engine.
     */
    private final EngineParameters parameters;

    /**
     * @return parameters of the engine.
     */
    public EngineParameters getParameters() {
        return parameters;
    }

    /**
     * Constructor of the Engine.
     *
     * @param classes list of classes to manage.
     */
    public CsvEngine(Class<?>... classes) {
        parameters = new EngineParameters();
        factory = new DefaultCsvManagerFactory();
        for (Class<?> clazz : classes) {
            scanClass(clazz);
        }
    }

    /**
     * Constructor of the engine.
     *
     * @param parameters parameters of engine (you can use :
     *                   {@link fr.ybonnel.csvengine.model.EngineParameters#createBuilder()}).<br/>
     *                   Example : EngineParameters parameters =
     *                   EngineParameters.createBuilder().setValidation(true).build();
     * @param classes    list of classes to manage.
     */
    public CsvEngine(EngineParameters parameters, Class<?>... classes) {
        this.parameters = parameters;
        factory = new DefaultCsvManagerFactory();
        for (Class<?> clazz : classes) {
            scanClass(clazz);
        }
    }

    /**
     * CSV Reader.
     */
    private AbstractCsvReader csvReader;

    /**
     * Used to construct a line based on the field.
     *
     * @param fields list of fields.
     * @return the CSV line.
     */
    private String constructLine(String[] fields) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String champ : fields) {
            if (first) {
                first = false;
            } else {
                builder.append(currentCsvClass.getSeparatorWithoutEscape());
            }
            builder.append(champ);
        }
        return builder.toString();
    }

    /**
     * Use to set a value of a field.
     *
     * @param csvField  the field to set.
     * @param csvObject the objects to set.
     * @param value     the value to set.
     * @throws fr.ybonnel.csvengine.validator.ValidateException if the value isn't good.
     */
    private void setValeur(CsvField csvField, Object csvObject, String value) throws ValidateException {
        csvField.getField().setAccessible(true);
        try {
            csvField.getField().set(csvObject, csvField.getAdapterCsv().parse(value));
        } catch (ValidateException exception) {
            throw exception;
        } catch (Exception e) {
            throw new ValidateException("Error in set", e);
        }
    }

    /**
     * Create an object from the current line of CSV.
     * {@link CsvEngine#newCsvFile} must be call before.
     *
     * @return the object created.
     * @throws fr.ybonnel.csvengine.validator.ValidationError
     *          in case of validation error.
     */
    protected Object createObject() throws ValidationError {
        if (currentCsvClass == null) {
            throw new CsvEngineException(
                    "The method \"createObject\" have been called without the call of the method newCsvFile.");
        }
        String[] fields = readLine();
        if (fields == null) {
            return null;
        }
        ValidationError validationError = null;
        Object csvObject = constructObject();
        for (int numChamp = 0; numChamp < fields.length; numChamp++) {
            validationError = processField(fields, validationError, csvObject, numChamp);
        }
        if (validationError != null) {
            throw validationError;
        }
        return csvObject;
    }

    /**
     * Process of a field.
     *
     * @param fieldsValues    list of flieds values.
     * @param validationError container of validation error.
     * @param csvObject       object to fill.
     * @param fieldNumber     number of the field.
     * @return container of validation error.
     */
    private ValidationError processField(String[] fieldsValues, ValidationError validationError,
                                         Object csvObject, int fieldNumber) {
        String fieldValue = fieldsValues[fieldNumber];
        if (fieldValue != null && !"".equals(fieldValue)) {
            validationError = fillField(fieldsValues, validationError, csvObject, fieldNumber, fieldValue);
        } else if (parameters.hasValidation()) {
            validationError = validateMandatoryField(fieldsValues, validationError, fieldNumber);
        }
        return validationError;
    }

    /**
     * Validation of the mandatory..
     *
     * @param fields          list of fields.
     * @param validationError container of validation errors.
     * @param fieldNumber     number of the field.
     * @return container of validation error.
     */
    private ValidationError validateMandatoryField(String[] fields, ValidationError validationError, int fieldNumber) {
        CsvField csvField = currentCsvClass.getCsvField(currentHeader[fieldNumber]);
        if (csvField != null && csvField.isMandatory()) {
            validationError =
                    addValidationMessage(fields, validationError, currentHeader[fieldNumber], new ValidateException(
                            "The field is mandatory"));
        }
        return validationError;
    }

    /**
     * Validate the field and fill it.
     *
     * @param fieldsValues    values of fields.
     * @param validationError container of validation errors.
     * @param csvObjects      object to fill.
     * @param fieldNumber     field number.
     * @param value           value of the field.
     * @return container of validation errors.
     */
    private ValidationError fillField(String[] fieldsValues, ValidationError validationError, Object csvObjects,
                                      int fieldNumber, String value) {
        String fieldName = currentHeader[fieldNumber];
        CsvField csvField = currentCsvClass.getCsvField(fieldName);
        if (csvField != null) {
            try {
                if (parameters.hasValidation()) {
                    csvField.validate(value);
                }
                setValeur(csvField, csvObjects, value);
            } catch (ValidateException exception) {
                validationError = addValidationMessage(
                        fieldsValues, validationError, currentHeader[fieldNumber], exception);
            }
        }
        return validationError;
    }

    /**
     * Add the validation message..
     *
     * @param fieldsValues    values of the field in CSV line.
     * @param validationError current validation error.
     * @param fieldName       field name.
     * @param exception       exception corresponding to the validation error.
     * @return the validation error.
     */
    private ValidationError addValidationMessage(String[] fieldsValues,
                                                 ValidationError validationError, String fieldName,
                                                 ValidateException exception) {
        if (validationError == null) {
            validationError = new ValidationError(constructLine(fieldsValues));
        }
        StringBuilder message = new StringBuilder("Validation error on the field ");
        message.append(fieldName);
        message.append(" : ");
        message.append(exception.getMessage());
        if (exception.getCause() != null) {
            message.append(" (cause : ");
            message.append(exception.getCause().getMessage());
            message.append(')');
        }
        validationError.getError().getMessages().add(message.toString());
        return validationError;
    }

    /**
     * Construct an object.
     *
     * @return constructed object.
     */
    private Object constructObject() {
        Object csvObject;
        try {
            csvObject = currentCsvClass.getConstructor().newInstance((Object[]) null);
        } catch (Exception exception) {
            Throwable myException = exception;
            if (myException instanceof InvocationTargetException) {
                myException = exception.getCause();
            }
            throw new CsvEngineException("Error during instantiation of "
                    + currentCsvClass.getClazz().getSimpleName(), myException);
        }
        return csvObject;
    }

    /**
     * Read of un line.
     *
     * @return read line.
     */
    private String[] readLine() {
        try {
            return csvReader.readLine();
        } catch (IOException e) {
            throw new CsvEngineException("Error reading a line", e);
        }
    }

    /**
     * Start the read of a new file.
     *
     * @param reader a Reader which represent the CSV File.
     * @param clazz  the associated class.
     */
    protected void newCsvFile(Reader reader, Class<?> clazz) {
        currentCsvClass = mapClasses.get(clazz);
        if (currentCsvClass == null) {
            throw new CsvEngineException("The class " + clazz.getSimpleName() + " isn't managed");
        }
        csvReader = factory.createReaderCsv(reader, currentCsvClass.getSeparatorWithoutEscape());
        try {
            currentHeader = csvReader.readLine();
        } catch (IOException e) {
            throw new CsvEngineException(e);
        }
        if (Character.isIdentifierIgnorable(currentHeader[0].charAt(0))) {
            currentHeader[0] = currentHeader[0].substring(1);
        }
    }

    /**
     * Close the current reader.
     */
    private void closeCurrentReader() {
        if (csvReader != null) {
            try {
                csvReader.close();
            } catch (IOException exception) {
                LOGGER.log(Level.WARNING, "Error closing CsvReader", exception);
            }
            csvReader = null;
        }
    }

    /**
     * Parse an InputStream representing a CSV File to transform it in a list of <T>.<br/>
     *
     * Default encoding is "UTF-8", you can use yours by using
     * {@link CsvEngine#parseInputStream(java.io.InputStream, java.nio.charset.Charset, Class)}
     *
     * @param <T>         Class associated to the CSV.
     * @param inputStream inputStream representing the CSV File.
     * @param clazz       class associated to the CSV File.
     * @return a result which contains errors end the list of <T> representing all rows of the CSV File.
     * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
     *          if the number of errors occurred exceed the accepted number
     *          {@link fr.ybonnel.csvengine.model.EngineParameters#getNbLinesWithErrorsToStop()}.
     */
    public <T> Result<T> parseInputStream(InputStream inputStream, Class<T> clazz)
            throws CsvErrorsExceededException {
        return parseInputStream(inputStream, Charset.forName("UTF-8"), clazz);
    }

    /**
     * Parse an InputStream representing a CSV File to transform it in a list of <T>.
     *
     * @param <T>         Class associated to the CSV.
     * @param inputStream inputStream representing the CSV File.
     * @param charset     class associated to the CSV File.
     * @param clazz       class associated to the CSV File.
     * @return a result which contains errors end the list of <T> representing all rows of the CSV File.
     * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
     *          if the number of errors occurred exceed the accepted number
     *          {@link fr.ybonnel.csvengine.model.EngineParameters#getNbLinesWithErrorsToStop()}.
     */
    public <T> Result<T> parseInputStream(InputStream inputStream, Charset charset, Class<T> clazz)
            throws CsvErrorsExceededException {
        Result<T> result = new Result<T>();
        result.getErrors().addAll(
                parseFileAndInsert(new BufferedReader(new InputStreamReader(inputStream, charset)), clazz,
                        new InsertInList<T>(result.getObjects())));
        return result;
    }

    /**
     * Parse an InputStream representing a CSV File to transform it in a list of <T><br/>.
     * Stop the parsing when nbLinesToParse is reached.
     *
     * @param <T>         Class associated to the CSV.
     * @param inputStream inputStream representing the CSV File.
     * @param clazz       class associated to the CSV File.
     * @param nbLinesToParse the maximum number of lines to parse.
     * @return a result which contains errors end the list of <T> representing all rows of the CSV File.
     * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
     *          if the number of errors occurred exceed the accepted number
     *          {@link fr.ybonnel.csvengine.model.EngineParameters#getNbLinesWithErrorsToStop()}.
     */
    public <T> Result<T> parseFirstLinesOfInputStream(InputStream inputStream, Class<T> clazz,
                                                      final int nbLinesToParse)
            throws CsvErrorsExceededException {
        final Result<T> result = new Result<T>();
        result.getErrors().addAll(parseFileAndInsert(new BufferedReader(new InputStreamReader(inputStream)), clazz,
                new InsertInList<T>(result.getObjects()) {
                    @Override
                    public void insertObject(T object) {
                        super.insertObject(object);
                        if (result.getObjects().size() >= nbLinesToParse) {
                            throw new StopParseException();
                        }
                    }
                }));

        return result;
    }

    /**
     * Parse a CSV File with an handler on each row.
     *
     * @param <T>     The class associated to the CSV File.
     * @param reader  a Reader which represent the CSV File.
     * @param clazz   class associated to the CSV File.
     * @param handler the handler call on each row.
     * @return the errors occurred.
     * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
     *          if the number of errors occurred exceed the accepted number
     *          {@link fr.ybonnel.csvengine.model.EngineParameters#getNbLinesWithErrorsToStop()}.
     */
    @SuppressWarnings("unchecked")
    public <T> List<Error> parseFileAndInsert(Reader reader, Class<T> clazz, InsertObject<T> handler)
            throws CsvErrorsExceededException {
        List<Error> errors = new ArrayList<Error>();
        try {
            newCsvFile(reader, clazz);
            T object = null;
            boolean hasValidationError;
            do {
                try {
                    hasValidationError = false;
                    object = (T) createObject();
                    if (object != null) {
                        handler.insertObject(object);
                    }
                } catch (ValidationError validationError) {
                    hasValidationError = true;
                    errors.add(validationError.getError());
                    if (parameters.getNbLinesWithErrorsToStop() >= 0
                            && errors.size() > parameters.getNbLinesWithErrorsToStop()) {
                        throw new CsvErrorsExceededException(errors);
                    }
                } catch (StopParseException stopParseException) {
                    return errors;
                }
            } while (object != null || hasValidationError);
        } finally {
            closeCurrentReader();
        }
        return errors;
    }

    /**
     * Parse a CSV file with an handler for batch of objects.
     * @param <T>     The class associated to the CSV File.
     * @param reader  a Reader which represent the CSV File.
     * @param clazz   class associated to the CSV File.
     * @param handler the handler call on batch of objects.
     * @param batchSize size of a batch.
     * @return the errors occurred.
     * @throws fr.ybonnel.csvengine.exception.CsvErrorsExceededException
     *          if the number of errors occurred exceed the accepted number
     *          {@link fr.ybonnel.csvengine.model.EngineParameters#getNbLinesWithErrorsToStop()}.
     */
    public <T> List<Error> parseFileAndHandleBatch(Reader reader, Class<T> clazz,
                                                   InsertBatch<T> handler, int batchSize)
            throws CsvErrorsExceededException {
        InsertObjectsForBatch<T> handlerBatch = new InsertObjectsForBatch<T>(handler, batchSize);
        List<Error> errors = parseFileAndInsert(reader, clazz, handlerBatch);
        if (!handlerBatch.getCurrentListOfObjects().isEmpty()) {
            handler.handleBatch(handlerBatch.getCurrentListOfObjects());
        }
        return errors;
    }

    /**
     * Scan a class to manage it in the engine.
     *
     * @param clazz class to scan.
     */
    protected void scanClass(Class<?> clazz) {
        CsvFile csvFile = clazz.getAnnotation(CsvFile.class);
        if (csvFile == null) {
            throw new CsvEngineException("Annotation CsvFile non present for class " + clazz.getSimpleName());
        }
        if (mapClasses.get(clazz) != null) {
            return;
        }
        CsvClass csvClass = new CsvClass(csvFile.separator(), clazz);
        for (Field field : clazz.getDeclaredFields()) {
            CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
            CsvValidation csvValidation = field.getAnnotation(CsvValidation.class);
            CsvValidations csvValidations = field.getAnnotation(CsvValidations.class);
            if (csvColumn != null) {
                csvClass.setCsvField(csvColumn.value(), new CsvField(csvColumn, csvValidations, csvValidation, field));
                csvClass.putOrder(csvColumn.value(), csvColumn.order());
            }
        }
        mapClasses.put(clazz, csvClass);
    }

    /**
     * Write a line into the CSV File.
     *
     * @param <T>         class associated to the CSV File.
     * @param csvWriter   writer to use.
     * @param fieldsNames list of the fields names.
     * @param csvClass    CsvClass associated to the CSV File.
     * @param object      Object to write in the CSV File.
     * @throws IllegalAccessException may not arrive.
     */
    private <T> void writeLine(AbstractCsvWriter csvWriter, List<String> fieldsNames, CsvClass csvClass, T object)
            throws IllegalAccessException {
        List<String> csvValues = new ArrayList<String>();
        for (String nomChamp : fieldsNames) {
            CsvField csvField = csvClass.getCsvField(nomChamp);
            csvField.getField().setAccessible(true);
            Object valeur = csvField.getField().get(object);
            csvField.getField().setAccessible(false);
            if (valeur != null) {
                csvValues.add(csvField.getAdapterCsv().toString(valeur));
            } else {
                csvValues.add(null);
            }
        }
        csvWriter.writeLine(csvValues);
    }

    /**
     * Write a CSV File based on an object list.
     *
     * @param <T>     Class associated to the CSV File.
     * @param writer  writer representing the CSV File.
     * @param objects objects list to write in CSV File.
     * @param clazz   Class associated to the CSV File.
     */
    public <T> void writeFile(Writer writer, Iterable<T> objects, Class<T> clazz) {
        try {
            final CsvClass csvClass = mapClasses.get(clazz);
            AbstractCsvWriter csvWriter =
                    factory.createWriterCsv(writer, csvClass.getSeparatorWithoutEscape(), parameters.hasAddQuoteCar());
            try {
                List<String> columnsNames = new ArrayList<String>();
                for (String columnName : csvClass.getColumnNames()) {
                    columnsNames.add(columnName);
                }
                Collections.sort(columnsNames, new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        int thisVal = csvClass.getOrder(o1);
                        int anotherVal = csvClass.getOrder(o2);
                        return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
                    }
                });
                csvWriter.writeLine(columnsNames);
                for (T object : objects) {
                    writeLine(csvWriter, columnsNames, csvClass, object);
                }
            } finally {
                csvWriter.close();
            }
        } catch (Exception exception) {
            throw new CsvEngineException(exception);
        }
    }

    /**
     * Get the CSV column names of a class.
     * @param clazz the class managed by CsvEngine.
     * @return list of column names of the class ordered
     * if the class use {@link fr.ybonnel.csvengine.annotation.CsvColumn#order()}.
     */
    public List<String> getColumnNames(Class clazz) {
        final CsvClass csvClass = mapClasses.get(clazz);
        if (csvClass == null) {
            throw new CsvEngineException("The class " + clazz.getSimpleName() + " isn't managed");
        }
        List<String> columnsNames = new ArrayList<String>();
        for (String columnName : csvClass.getColumnNames()) {
            columnsNames.add(columnName);
        }
        Collections.sort(columnsNames, new Comparator<String>() {
            public int compare(String o1, String o2) {
                int thisVal = csvClass.getOrder(o1);
                int anotherVal = csvClass.getOrder(o2);
                return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
            }
        });
        return columnsNames;
    }
}
