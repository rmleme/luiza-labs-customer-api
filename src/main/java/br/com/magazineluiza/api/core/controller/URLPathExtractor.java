package br.com.magazineluiza.api.core.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class URLPathExtractor {

	private static final String FAVORITE_PRODUCT_RESOURCE = "favoriteproduct";

	private static final Pattern REGEX_PATTERN_ID = Pattern
			.compile("^\\/([0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12})(\\/"
					+ FAVORITE_PRODUCT_RESOURCE + ")*$");

	private String customerId;

	private boolean isFavoriteProductRequest;

	public URLPathExtractor(HttpServletRequest request) throws ServletException {
		if (request.getPathInfo() == null) {
			return;
		}

		Matcher matcher = REGEX_PATTERN_ID.matcher(request.getPathInfo());
		if (matcher.find()) {
			customerId = matcher.group(1);
			isFavoriteProductRequest = matcher.group(2) != null;
			return;
		}

		// TODO: trocar por um ValidationError
		throw new ServletException("Invalid URI");
	}

	public String getId() {
		return customerId;
	}

	public boolean isFavoriteProductRequest() {
		return isFavoriteProductRequest;
	}
}