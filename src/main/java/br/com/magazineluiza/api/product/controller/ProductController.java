package br.com.magazineluiza.api.product.controller;

import static br.com.magazineluiza.api.core.controller.ControllerUtils.writeResponse;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.jr.ob.JSON;

import br.com.magazineluiza.api.core.model.ErrorMessage;
import br.com.magazineluiza.api.core.validator.ValidationException;
import br.com.magazineluiza.api.customer.model.Customer;
import br.com.magazineluiza.api.customer.service.CustomerService;
import br.com.magazineluiza.api.product.model.Product;

public class ProductController extends HttpServlet {

	private static final long serialVersionUID = -1356337289359120723L;

	private static final Logger logger = LogManager.getLogger();

	private CustomerService customerService;

	public ProductController(CustomerService customerService) {
		this.customerService = customerService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String payload = (String) request.getAttribute("payload");
		String customerId = (String) request.getAttribute("customerId");

		logger.debug("New favorite product: {}", payload);

		Customer customer = customerService.retrieve(customerId);

		if (customer == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		Product product = JSON.std.beanFrom(Product.class, payload);

		Product newProduct = null;
		try {
			newProduct = customerService.addFavoriteProduct(customer, product);
		} catch (ValidationException e) {
			logger.error("Could not add product {}: validation failed with {}.", product, e.getError());
			ErrorMessage message = new ErrorMessage(e.getError().getErrorMessage());
			writeResponse(message, response, e.getError().getStatusCode());
			return;
		}

		writeResponse(newProduct, response, HttpServletResponse.SC_CREATED);
	}

	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String customerId = (String) request.getAttribute("customerId");
		String productId = (String) request.getAttribute("productId");

		logger.debug("Product id: {}", customerId);

		if (customerId == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		Customer customer = customerService.retrieve(customerId);

		if (customer == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		int numberOfRemovedProducts = customerService.removeFavoriteProduct(customer, productId);

		if (numberOfRemovedProducts == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
}