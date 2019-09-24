package br.com.magazineluiza.api.customer.validator;

import static br.com.magazineluiza.api.customer.validator.ValidationError.NAME_BLANK;
import static br.com.magazineluiza.api.customer.validator.ValidationError.NAME_INVALID_SIZE;

import org.apache.commons.lang3.StringUtils;

import br.com.magazineluiza.api.customer.model.Customer;

public class NameValidator {

	public void validate(String name) throws ValidationException {
		if (StringUtils.isBlank(name)) {
			throw new ValidationException(NAME_BLANK);
		}

		if (name.length() > Customer.MAX_NAME_SIZE) {
			throw new ValidationException(NAME_INVALID_SIZE);
		}
	}
}