package br.com.magazineluiza.api.product.validator;

import static br.com.magazineluiza.api.core.validator.ValidationError.PRODUCT_ID_BLANK;
import static br.com.magazineluiza.api.core.validator.ValidationError.PRODUCT_NOT_FOUND;

import org.apache.commons.lang3.StringUtils;

import br.com.magazineluiza.api.core.validator.ValidationException;
import br.com.magazineluiza.api.product.client.ProductClient;

public class ProductValidator {

	private ProductClient productClient;

	public ProductValidator(ProductClient productClient) {
		this.productClient = productClient;
	}

	public void validate(String id) throws ValidationException {
		if (StringUtils.isBlank(id)) {
			throw new ValidationException(PRODUCT_ID_BLANK);
		}

		if (!productClient.exists(id)) {
			throw new ValidationException(PRODUCT_NOT_FOUND);
		}
	}
}