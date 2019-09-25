package br.com.magazineluiza.api.customer.controller;

import static br.com.magazineluiza.api.core.controller.ControllerUtils.APPLICATION_JSON;
import static br.com.magazineluiza.api.core.controller.ControllerUtils.writeResponse;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.jr.ob.JSON;

import br.com.magazineluiza.api.core.controller.URLPathExtractor;
import br.com.magazineluiza.api.core.model.ErrorMessage;
import br.com.magazineluiza.api.core.validator.ValidationException;
import br.com.magazineluiza.api.customer.model.Customer;
import br.com.magazineluiza.api.customer.service.CustomerService;
import br.com.magazineluiza.api.product.controller.ProductController;

@WebServlet(urlPatterns = { "/customer/*" }, asyncSupported = true)
@ServletSecurity(value = @HttpConstraint(rolesAllowed = { "CUSTOMER_READ_ROLE" }), httpMethodConstraints = {
        @HttpMethodConstraint(value = "POST", rolesAllowed = "CUSTOMER_PERSIST_ROLE"),
        @HttpMethodConstraint(value = "PUT", rolesAllowed = "CUSTOMER_PERSIST_ROLE"),
        @HttpMethodConstraint(value = "DELETE", rolesAllowed = "CUSTOMER_PERSIST_ROLE"), })
public class CustomerController extends HttpServlet {

	private static final long serialVersionUID = 8654517234459083979L;

	private static final Logger logger = LogManager.getLogger();

	@Resource(name = "jdbc/CustomerDB")
	private DataSource dataSource;

	private CustomerService customerService;

	private ProductController productController;

	@Override
	public void init() throws ServletException {
		customerService = new CustomerService(dataSource);
		productController = new ProductController(customerService);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final AsyncContext context = request.startAsync();
		context.start(new Runnable() {
			public void run() {
				HttpServletRequest httpRequest = (HttpServletRequest) context.getRequest();
				HttpServletResponse httpResponse = (HttpServletResponse) context.getResponse();

				try {
					if (!APPLICATION_JSON.equals(httpRequest.getContentType())) {
						httpResponse.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
						return;
					}

					String payload = IOUtils.toString(httpRequest.getReader());
					if (payload == null || StringUtils.isBlank(payload)) {
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}

					URLPathExtractor pathExtractor = new URLPathExtractor(httpRequest);
					if (!pathExtractor.isValidRequest()) {
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}

					if (pathExtractor.isFavoriteProductRequest()) {
						httpRequest.setAttribute("payload", payload);
						httpRequest.setAttribute("customerId", pathExtractor.getCustomerId());
						productController.doPost(httpRequest, httpResponse);
						return;
					}

					logger.debug("New customer: {}", payload);

					Customer customer = JSON.std.beanFrom(Customer.class, payload);

					Customer newCustomer = null;
					try {
						newCustomer = customerService.create(customer);
					} catch (ValidationException e) {
						logger.error("Could not create customer {}: validation failed with {}.", customer,
								e.getError());
						ErrorMessage message = new ErrorMessage(e.getError().getErrorMessage());
						writeResponse(message, httpResponse, e.getError().getStatusCode());
						return;
					}

					writeResponse(newCustomer, httpResponse, HttpServletResponse.SC_CREATED);
				} catch (Exception e) {
					logger.error(e);
				} finally {
					context.complete();
				}
			}
		});
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final AsyncContext context = request.startAsync();
		context.start(new Runnable() {
			public void run() {
				HttpServletRequest httpRequest = (HttpServletRequest) context.getRequest();
				HttpServletResponse httpResponse = (HttpServletResponse) context.getResponse();

				try {
					URLPathExtractor pathExtractor = new URLPathExtractor(httpRequest);
					if (!pathExtractor.isValidRequest()) {
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
					logger.debug("Customer id: {}", pathExtractor.getCustomerId());

					if (pathExtractor.getCustomerId() == null) {
						List<Customer> customers = customerService.retrieveAll();

						writeResponse(customers, httpResponse, HttpServletResponse.SC_OK);
					} else {
						Customer customer = customerService.retrieve(pathExtractor.getCustomerId());

						if (customer == null) {
							httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
							return;
						}

						writeResponse(customer, httpResponse, HttpServletResponse.SC_OK);
					}
				} catch (Exception e) {
					logger.error(e);
				} finally {
					context.complete();
				}
			}
		});
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final AsyncContext context = request.startAsync();
		context.start(new Runnable() {
			public void run() {
				HttpServletRequest httpRequest = (HttpServletRequest) context.getRequest();
				HttpServletResponse httpResponse = (HttpServletResponse) context.getResponse();

				try {
					URLPathExtractor pathExtractor = new URLPathExtractor(httpRequest);
					if (!pathExtractor.isValidRequest()) {
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
					logger.debug("Customer id: {}", pathExtractor.getCustomerId());

					if (pathExtractor.getCustomerId() == null) {
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}

					if (!APPLICATION_JSON.equals(httpRequest.getContentType())) {
						httpResponse.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
						return;
					}

					String payload = IOUtils.toString(httpRequest.getReader());
					logger.debug("Customer: {}", payload);

					if (payload == null || StringUtils.isBlank(payload)) {
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}

					Customer customer = JSON.std.beanFrom(Customer.class, payload);
					customer.setId(pathExtractor.getCustomerId());

					int numberOfUpdatedCustomers = 0;
					try {
						numberOfUpdatedCustomers = customerService.update(customer);
					} catch (ValidationException e) {
						logger.error("Could not update customer {}: validation failed with {}.", customer,
								e.getError());
						ErrorMessage message = new ErrorMessage(e.getError().getErrorMessage());
						writeResponse(message, httpResponse, e.getError().getStatusCode());
						return;
					}

					if (numberOfUpdatedCustomers == 0) {
						httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
						return;
					}

					writeResponse(customer, httpResponse, HttpServletResponse.SC_OK);
				} catch (Exception e) {
					logger.error(e);
				} finally {
					context.complete();
				}
			}
		});
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final AsyncContext context = request.startAsync();
		context.start(new Runnable() {
			public void run() {
				HttpServletRequest httpRequest = (HttpServletRequest) context.getRequest();
				HttpServletResponse httpResponse = (HttpServletResponse) context.getResponse();

				try {
					URLPathExtractor pathExtractor = new URLPathExtractor(httpRequest);
					if (!pathExtractor.isValidRequest()) {
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}

					if (pathExtractor.isFavoriteProductRequest()) {
						httpRequest.setAttribute("customerId", pathExtractor.getCustomerId());
						httpRequest.setAttribute("productId", pathExtractor.getProductId());
						productController.doDelete(httpRequest, httpResponse);
						return;
					}

					logger.debug("Customer id: {}", pathExtractor.getCustomerId());

					if (pathExtractor.getCustomerId() == null) {
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}

					int numberOfDeletedCustomers = customerService.delete(pathExtractor.getCustomerId());

					if (numberOfDeletedCustomers == 0) {
						httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
						return;
					}

					httpResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
				} catch (Exception e) {
					logger.error(e);
				} finally {
					context.complete();
				}
			}
		});
	}
}