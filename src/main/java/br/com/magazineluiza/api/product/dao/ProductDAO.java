package br.com.magazineluiza.api.product.dao;

import static br.com.magazineluiza.api.core.validator.ValidationError.PRODUCT_NOT_UNIQUE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.magazineluiza.api.core.dao.UUIDConverter;
import br.com.magazineluiza.api.core.validator.ValidationException;
import br.com.magazineluiza.api.customer.model.Customer;
import br.com.magazineluiza.api.product.model.Product;

public class ProductDAO {

	private static final Logger logger = LogManager.getLogger();

	private DataSource dataSource;

	private ProductMapper productMapper;

	public ProductDAO(DataSource dataSource) {
		this.dataSource = dataSource;
		this.productMapper = new ProductMapper();
	}

	public Product addFavoriteProduct(Customer customer, Product product) throws ValidationException {
		String sql = "INSERT INTO product (customer_id, id, price, image, brand, title, review_score) SELECT customer_id, ?, ?, ?, ?, ?, ? FROM customer WHERE id = ?";

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setBytes(1, UUIDConverter.convertUUIDToBinary(product.getId()));
			ps.setDouble(2, product.getPrice());
			ps.setString(3, product.getImage());
			ps.setString(4, product.getBrand());
			ps.setString(5, product.getTitle());
			if (product.getReviewScore() == null) {
				ps.setNull(6, Types.DOUBLE);
			} else {
				ps.setDouble(6, product.getReviewScore());
			}
			ps.setBytes(7, UUIDConverter.convertUUIDToBinary(customer.getId()));
			ps.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new ValidationException(e, PRODUCT_NOT_UNIQUE);
		} catch (SQLException e) {
			logger.error("Could not add product.", e);
		}

		return product;
	}

	public Set<Product> retrieveByCustomer(Customer customer) {
		String sql = "SELECT p.id, p.price, p.image, p.brand, p.title, p.review_score FROM product p, customer c WHERE p.customer_id = c.customer_id AND c.id = ?";
		Set<Product> products = new HashSet<>();

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setBytes(1, UUIDConverter.convertUUIDToBinary(customer.getId()));
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					products.add(productMapper.toProduct(rs));
				}
			}
		} catch (SQLException e) {
			logger.error("Could not retrieve products.", e);
		}

		return products;
	}

	public int removeFavoriteProduct(Customer customer, String id) {
		String sql = "DELETE FROM product WHERE customer_id = (SELECT customer_id FROM customer WHERE id = ?) AND id = ?";
		int rowCount = 0;

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setBytes(1, UUIDConverter.convertUUIDToBinary(customer.getId()));
			ps.setBytes(2, UUIDConverter.convertUUIDToBinary(id));
			rowCount = ps.executeUpdate();
			if (rowCount != 1) {
				logger.warn("Product not removed.");
			}
		} catch (SQLException e) {
			logger.error("Could not remove product.", e);
		}

		return rowCount;
	}
}