package br.com.magazineluiza.api.product.validator;

import static br.com.magazineluiza.api.core.validator.ValidationError.PRODUCT_ID_BLANK;

import org.apache.commons.lang3.StringUtils;

import br.com.magazineluiza.api.core.validator.ValidationException;

public class ProductValidator {

	public void validate(String id) throws ValidationException {
		if (StringUtils.isBlank(id)) {
			throw new ValidationException(PRODUCT_ID_BLANK);
		}
	}
}