package br.com.magazineluiza.api.customer.service;

import java.util.List;

import javax.sql.DataSource;

import br.com.magazineluiza.api.customer.dao.CustomerDAO;
import br.com.magazineluiza.api.customer.model.Customer;
import br.com.magazineluiza.api.customer.validator.EmailValidator;
import br.com.magazineluiza.api.customer.validator.NameValidator;
import br.com.magazineluiza.api.customer.validator.ValidationException;

public class CustomerService {

	private CustomerDAO customerDAO;

	private EmailValidator emailValidator;

	private NameValidator nameValidator;

	public CustomerService(DataSource dataSource) {
		this.customerDAO = new CustomerDAO(dataSource);
		this.emailValidator = new EmailValidator(this.customerDAO);
		this.nameValidator = new NameValidator();
	}

	public Customer create(Customer customer) throws ValidationException {
		emailValidator.validate(customer.getEmail());
		nameValidator.validate(customer.getName());

		return customerDAO.create(customer);
	}

	public List<Customer> retrieveAll() {
		return customerDAO.retrieveAll();
	}

	public Customer retrieve(long id) {
		return customerDAO.retrieve(id);
	}

	public int update(Customer customer) throws ValidationException {
		emailValidator.validate(customer.getEmail());
		nameValidator.validate(customer.getName());

		return customerDAO.update(customer);
	}

	public int delete(long id) {
		return customerDAO.delete(id);
	}
}