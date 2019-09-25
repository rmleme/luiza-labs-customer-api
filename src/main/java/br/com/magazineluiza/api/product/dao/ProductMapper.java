package br.com.magazineluiza.api.product.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.magazineluiza.api.core.dao.UUIDConverter;
import br.com.magazineluiza.api.product.model.Product;

public class ProductMapper {

	public Product toProduct(ResultSet rs) throws SQLException {
		Product product = new Product();
		product.setId(UUIDConverter.convertBinaryToUUID(rs.getBytes(1)));
		product.setPrice(rs.getDouble(2));
		product.setImage(rs.getString(3));
		product.setBrand(rs.getString(4));
		product.setTitle(rs.getString(5));
		Double reviewScore = rs.getDouble(6);
		product.setReviewScore(rs.wasNull() ? null : reviewScore);
		return product;
	}
}