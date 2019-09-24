package br.com.magazineluiza.api.customer.validator;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 1935336907990190381L;

	private ValidationError error;

	public ValidationException(ValidationError error) {
		this.error = error;
	}

	public ValidationException(String message, ValidationError error) {
		super(message);
		this.error = error;
	}

	public ValidationException(Throwable cause, ValidationError error) {
		super(cause);
		this.error = error;
	}

	public ValidationException(String message, Throwable cause, ValidationError error) {
		super(message, cause);
		this.error = error;
	}

	public ValidationError getError() {
		return error;
	}
}