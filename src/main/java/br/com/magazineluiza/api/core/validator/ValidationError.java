package br.com.magazineluiza.api.core.validator;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CONFLICT;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import java.util.ResourceBundle;

public enum ValidationError {

	CUSTOMER_EMAIL_BLANK("customer.field.email.blank", SC_BAD_REQUEST),
	CUSTOMER_EMAIL_INVALID_SIZE("customer.field.email.invalid.size", SC_BAD_REQUEST),
	CUSTOMER_EMAIL_INVALID_FORMAT("customer.field.email.invalid.format", SC_BAD_REQUEST),
	CUSTOMER_EMAIL_NOT_UNIQUE("customer.field.email.not.unique", SC_CONFLICT),
	CUSTOMER_NAME_BLANK("customer.field.name.blank", SC_BAD_REQUEST),
	CUSTOMER_NAME_INVALID_SIZE("customer.field.name.invalid.size", SC_BAD_REQUEST),
	PRODUCT_ID_BLANK("product.field.id.blank", SC_BAD_REQUEST),
	PRODUCT_NOT_FOUND("product.field.id.not.found", SC_NOT_FOUND),
	PRODUCT_NOT_UNIQUE("product.field.id.not.unique", SC_CONFLICT);

	private static final String RESOURCE_BUNDLE_NAME = "messages";

	private ResourceBundle errorMessages;

	private String errorKey;

	private int statusCode;

	private ValidationError(String errorKey, int statusCode) {
		this.errorMessages = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
		this.errorKey = errorKey;
		this.statusCode = statusCode;
	}

	public String getErrorMessage() {
		return errorMessages.getString(errorKey);
	}

	public int getStatusCode() {
		return statusCode;
	}
}