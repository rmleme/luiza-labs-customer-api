package br.com.magazineluiza.api.customer.validator;

import static br.com.magazineluiza.api.core.validator.ValidationError.CUSTOMER_EMAIL_BLANK;
import static br.com.magazineluiza.api.core.validator.ValidationError.CUSTOMER_EMAIL_INVALID_FORMAT;
import static br.com.magazineluiza.api.core.validator.ValidationError.CUSTOMER_EMAIL_INVALID_SIZE;
import static br.com.magazineluiza.api.core.validator.ValidationError.CUSTOMER_EMAIL_NOT_UNIQUE;

import org.apache.commons.lang3.StringUtils;

import br.com.magazineluiza.api.core.validator.ValidationException;
import br.com.magazineluiza.api.customer.dao.CustomerDAO;
import br.com.magazineluiza.api.customer.model.Customer;

public class EmailValidator {

	private org.apache.commons.validator.routines.EmailValidator emailValidator;

	private CustomerDAO customerDAO;

	public EmailValidator(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
		this.emailValidator = org.apache.commons.validator.routines.EmailValidator.getInstance();
	}

	public void validate(String email) throws ValidationException {
		if (StringUtils.isBlank(email)) {
			throw new ValidationException(CUSTOMER_EMAIL_BLANK);
		}

		if (email.length() > Customer.MAX_EMAIL_SIZE) {
			throw new ValidationException(CUSTOMER_EMAIL_INVALID_SIZE);
		}

		if (!emailValidator.isValid(email)) {
			throw new ValidationException(CUSTOMER_EMAIL_INVALID_FORMAT);
		}

		if (customerDAO.existsByEmail(email)) {
			throw new ValidationException(CUSTOMER_EMAIL_NOT_UNIQUE);
		}
	}
}