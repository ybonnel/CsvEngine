package fr.ybo.moteurcsv.factory;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

public class EcritureOpenCsv extends AbstractEcritureCsv {

	private CSVWriter csvWriter;

	public EcritureOpenCsv(Writer writer, char separateur) {
		this.csvWriter = new CSVWriter(writer, separateur);
	}

	@Override
	public void writeLine(List<String> champs) {
		csvWriter.writeNext(champs.toArray(new String[champs.size()]));
	}

	@Override
	public void close() throws IOException {
		csvWriter.close();
	}
}
