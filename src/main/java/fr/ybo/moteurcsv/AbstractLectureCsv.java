package fr.ybo.moteurcsv;

import java.io.Closeable;
import java.io.IOException;

public abstract class AbstractLectureCsv implements Closeable {
	
	public abstract String[] readLine() throws IOException;

}
