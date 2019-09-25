package br.com.magazineluiza.api.customer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.magazineluiza.api.core.dao.UUIDConverter;
import br.com.magazineluiza.api.customer.model.Customer;
import br.com.magazineluiza.api.product.dao.ProductDAO;

public class CustomerDAO {

	private static final Logger logger = LogManager.getLogger();

	private DataSource dataSource;

	private ProductDAO productDAO;

	private CustomerMapper customerMapper;

	public CustomerDAO(DataSource dataSource) {
		this.dataSource = dataSource;
		this.productDAO = new ProductDAO(this.dataSource);
		this.customerMapper = new CustomerMapper();
	}

	public Customer create(Customer customer) {
		String sql = "INSERT INTO customer (id, name, email) VALUES (?, ?, ?)";
		String id = UUID.randomUUID().toString();

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setBytes(1, UUIDConverter.convertUUIDToBinary(id));
			ps.setString(2, customer.getName());
			ps.setString(3, customer.getEmail());
			ps.executeUpdate();
			customer.setId(id);
		} catch (SQLException e) {
			logger.error("Could not create new customer.", e);
		}

		return customer;
	}

	public List<Customer> retrieveAll() {
		String sql = "SELECT id, name, email FROM customer";
		List<Customer> customers = new ArrayList<>();

		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();) {
			while (rs.next()) {
				Customer customer = customerMapper.toCustomer(rs);
				customer.setFavoriteProducts(productDAO.retrieveByCustomer(customer));
				customers.add(customer);
			}
		} catch (SQLException e) {
			logger.error("Could not retrieve customers.", e);
		}

		return customers;
	}

	public Customer retrieve(String id) {
		String sql = "SELECT id, name, email FROM customer WHERE id = ?";
		Customer customer = null;

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setBytes(1, UUIDConverter.convertUUIDToBinary(id));
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					customer = customerMapper.toCustomer(rs);
					customer.setFavoriteProducts(productDAO.retrieveByCustomer(customer));
				}
			}
		} catch (SQLException e) {
			logger.error("Could not retrieve customer.", e);
		}

		return customer;
	}

	public int update(Customer customer) {
		String sql = "UPDATE customer SET name = ?, email = ? WHERE id = ?";
		int rowCount = 0;

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setString(1, customer.getName());
			ps.setString(2, customer.getEmail());
			ps.setBytes(3, UUIDConverter.convertUUIDToBinary(customer.getId()));
			rowCount = ps.executeUpdate();
			if (rowCount != 1) {
				logger.warn("Customer not updated.");
			}
		} catch (SQLException e) {
			logger.error("Could not update customer.", e);
		}

		return rowCount;
	}

	public int delete(String id) {
		String sql = "DELETE FROM customer WHERE id = ?";
		int rowCount = 0;

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setBytes(1, UUIDConverter.convertUUIDToBinary(id));
			rowCount = ps.executeUpdate();
			if (rowCount != 1) {
				logger.warn("Customer not deleted.");
			}
		} catch (SQLException e) {
			logger.error("Could not delete customer.", e);
		}

		return rowCount;
	}

	public boolean existsByEmail(String email) {
		String sql = "SELECT COUNT(*) FROM customer WHERE email = ?";
		boolean existsCustomer = false;

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					existsCustomer = rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			logger.error("Could not count customer.", e);
		}

		return existsCustomer;
	}
}