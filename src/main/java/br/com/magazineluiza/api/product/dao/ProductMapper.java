package br.com.magazineluiza.api.product.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.magazineluiza.api.core.dao.UUIDConverter;
import br.com.magazineluiza.api.product.model.Product;

public class ProductMapper {

	public Product toProduct(ResultSet rs) throws SQLException {
		Product product = new Product();
		product.setId(UUIDConverter.convertBinaryToUUID(rs.getBytes(1)));
		return product;
	}
}