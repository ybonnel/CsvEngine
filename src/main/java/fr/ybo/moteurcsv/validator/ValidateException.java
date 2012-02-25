package fr.ybo.moteurcsv.validator;

public class ValidateException extends Exception {

	private static final long serialVersionUID = 1L;

	public ValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidateException(String message) {
		super(message);
	}
}
