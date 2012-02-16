package fr.ybo.moteurcsv.factory;

import java.io.Reader;
import java.io.Writer;


public interface GestionnaireCsvFactory {

	AbstractEcritureCsv createWriterCsv(Writer writer, char separator);

	AbstractLectureCsv createReaderCsv(Reader reader, char separator);

}
