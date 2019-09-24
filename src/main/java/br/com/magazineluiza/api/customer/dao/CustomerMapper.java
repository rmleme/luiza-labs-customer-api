package br.com.magazineluiza.api.customer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.DatatypeConverter;

import br.com.magazineluiza.api.customer.model.Customer;

public class CustomerMapper {

	public Customer toCustomer(ResultSet rs) throws SQLException {
		Customer customer = new Customer();
		customer.setId(convertBinaryToUUID(rs.getBytes(1)));
		customer.setName(rs.getString(2));
		customer.setEmail(rs.getString(3));
		return customer;
	}

	private String convertBinaryToUUID(byte[] binaryValue) {
		String uuidValue = DatatypeConverter.printHexBinary(binaryValue).toLowerCase();
		return uuidValue.substring(0, 8) + "-" + uuidValue.substring(8, 12) + "-" + uuidValue.substring(12, 16) + "-"
				+ uuidValue.substring(16, 20) + "-" + uuidValue.substring(20);
	}
}