package br.com.magazineluiza.api.product.model;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class Product {

	private double price;

	private String image;

	private String brand;

	private String id;

	private String title;

	private Double reviewScore;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(Double reviewScore) {
		this.reviewScore = reviewScore;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (!(o instanceof Product)) {
			return false;
		}
		Product other = (Product) o;
		return StringUtils.equals(this.id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.id);
	}

	@Override
	public String toString() {
		return "Product [price=" + price + ", image=" + image + ", brand=" + brand + ", id=" + id + ", title=" + title
				+ ", reviewScore=" + reviewScore + "]";
	}
}