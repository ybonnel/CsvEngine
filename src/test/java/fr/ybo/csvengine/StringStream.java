package fr.ybo.csvengine;

import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream permettant de simuler.
 * 
 * @author ybonnel
 * 
 */
public class StringStream extends InputStream {
	private String chaine;
	private int count = 0;

	@Override
	public int read() throws IOException {
		if (count >= chaine.length()) {
			return -1;
		}
		return chaine.charAt(count++);
	}

	public StringStream(String chaine) {
		super();
		this.chaine = chaine;
	}

}