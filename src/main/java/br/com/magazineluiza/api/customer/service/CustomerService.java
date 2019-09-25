package br.com.magazineluiza.api.customer.service;

import static br.com.magazineluiza.api.core.validator.ValidationError.PRODUCT_NOT_FOUND;

import java.util.List;

import javax.sql.DataSource;

import br.com.magazineluiza.api.core.validator.ValidationException;
import br.com.magazineluiza.api.customer.dao.CustomerDAO;
import br.com.magazineluiza.api.customer.model.Customer;
import br.com.magazineluiza.api.customer.validator.EmailValidator;
import br.com.magazineluiza.api.customer.validator.NameValidator;
import br.com.magazineluiza.api.product.client.ProductClient;
import br.com.magazineluiza.api.product.dao.ProductDAO;
import br.com.magazineluiza.api.product.model.Product;
import br.com.magazineluiza.api.product.validator.ProductValidator;

public class CustomerService {

	private CustomerDAO customerDAO;

	private ProductDAO productDAO;

	private ProductClient productClient;

	private EmailValidator emailValidator;

	private NameValidator nameValidator;

	private ProductValidator productValidator;

	public CustomerService(DataSource dataSource) {
		this.customerDAO = new CustomerDAO(dataSource);
		this.productDAO = new ProductDAO(dataSource);
		this.productClient = new ProductClient();
		this.emailValidator = new EmailValidator(this.customerDAO);
		this.nameValidator = new NameValidator();
		this.productValidator = new ProductValidator();
	}

	public Customer create(Customer customer) throws ValidationException {
		emailValidator.validate(customer.getEmail());
		nameValidator.validate(customer.getName());

		return customerDAO.create(customer);
	}

	public List<Customer> retrieveAll() {
		return customerDAO.retrieveAll();
	}

	public Customer retrieve(String id) {
		return customerDAO.retrieve(id);
	}

	public int update(Customer customer) throws ValidationException {
		emailValidator.validate(customer.getEmail());
		nameValidator.validate(customer.getName());

		return customerDAO.update(customer);
	}

	public int delete(String id) {
		return customerDAO.delete(id);
	}

	public Product addFavoriteProduct(Customer customer, Product product) throws ValidationException {
		productValidator.validate(product.getId());

		Product newProduct = productClient.retrieve(product.getId());
		if (newProduct == null) {
			throw new ValidationException(PRODUCT_NOT_FOUND);
		}

		customer.getFavoriteProducts().add(newProduct);
		return productDAO.addFavoriteProduct(customer, newProduct);
	}

	public int removeFavoriteProduct(Customer customer, String productId) {
		customer.getFavoriteProducts().remove(new Product(productId));
		return productDAO.removeFavoriteProduct(customer, productId);
	}
}