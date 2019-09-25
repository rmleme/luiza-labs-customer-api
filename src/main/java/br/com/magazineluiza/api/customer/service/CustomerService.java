package br.com.magazineluiza.api.customer.service;

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

	private EmailValidator emailValidator;

	private NameValidator nameValidator;

	private ProductValidator productValidator;

	public CustomerService(DataSource dataSource) {
		this.customerDAO = new CustomerDAO(dataSource);
		this.productDAO = new ProductDAO(dataSource);
		this.emailValidator = new EmailValidator(this.customerDAO);
		this.nameValidator = new NameValidator();
		this.productValidator = new ProductValidator(new ProductClient());
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

		customer.getFavoriteProducts().add(product);
		productDAO.addFavoriteProduct(customer, product);
		return product;
	}
}