package fr.ybo.moteurcsv.factory;

import java.io.Closeable;
import java.io.IOException;

public abstract class AbstractLectureCsv implements Closeable {
	
	public abstract String[] readLine() throws IOException;

}
