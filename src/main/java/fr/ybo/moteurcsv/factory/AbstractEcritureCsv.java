package fr.ybo.moteurcsv.factory;

import java.io.Closeable;
import java.util.List;

public abstract class AbstractEcritureCsv implements Closeable {

	public abstract void writeLine(List<String> champs);

}
