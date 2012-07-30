package fr.ybo.csvengine;

import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream used tu simulate a file.
 * 
 * @author ybonnel
 * 
 */
public class StringStream extends InputStream {
	private String string;
	private int count = 0;

	@Override
	public int read() throws IOException {
		if (count >= string.length()) {
			return -1;
		}
		return string.charAt(count++);
	}

	public StringStream(String string) {
		super();
		this.string = string;
	}

}