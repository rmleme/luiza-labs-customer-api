package br.com.magazineluiza.api.customer.model;

import java.util.HashSet;
import java.util.Set;

import br.com.magazineluiza.api.product.model.Product;

public class Customer {

	public static final int MAX_NAME_SIZE = 100;

	public static final int MAX_EMAIL_SIZE = 100;

	private String id;

	private String name;

	private String email;

	private Set<Product> favoriteProducts = new HashSet<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Product> getFavoriteProducts() {
		return favoriteProducts;
	}

	public void setFavoriteProducts(Set<Product> favoriteProducts) {
		this.favoriteProducts = favoriteProducts;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", email=" + email + ", favoriteProducts=" + favoriteProducts
				+ "]";
	}
}