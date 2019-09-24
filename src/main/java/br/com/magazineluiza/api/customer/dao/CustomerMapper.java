package br.com.magazineluiza.api.customer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.magazineluiza.api.customer.model.Customer;

public class CustomerMapper {

	public Customer toCustomer(ResultSet rs) throws SQLException {
		Customer customer = new Customer();
		customer.setId(rs.getInt(1));
		customer.setName(rs.getString(2));
		customer.setEmail(rs.getString(3));
		return customer;
	}
}