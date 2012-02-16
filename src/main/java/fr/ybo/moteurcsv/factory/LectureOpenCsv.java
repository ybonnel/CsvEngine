package fr.ybo.moteurcsv.factory;

import java.io.IOException;
import java.io.Reader;

import au.com.bytecode.opencsv.CSVReader;

public class LectureOpenCsv extends AbstractLectureCsv {
	
	private CSVReader reader;
	
	public LectureOpenCsv(Reader reader, char separator) {
		super();
		this.reader = new CSVReader(reader, separator);
	}
	
	@Override
	public String[] readLine() throws IOException {
		return reader.readNext();
	}

	public void close() throws IOException {
		reader.close();
	}
}
