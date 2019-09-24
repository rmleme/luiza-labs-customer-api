package br.com.magazineluiza.api.customer.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class CustomerURLPathExtractor {

	private static final Pattern REGEX_PATTERN_ID = Pattern.compile("/([0-9]+)");

	private Integer id = null;

	public CustomerURLPathExtractor(HttpServletRequest request) throws ServletException {
		if (request.getPathInfo() == null) {
			return;
		}

		Matcher matcher = REGEX_PATTERN_ID.matcher(request.getPathInfo());
		if (matcher.find()) {
			id = Integer.valueOf(matcher.group(1));
			return;
		}

		throw new ServletException("Invalid URI");
	}

	public Integer getId() {
		return id;
	}
}