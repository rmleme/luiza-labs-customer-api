package br.com.magazineluiza.api.customer.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class CustomerURLPathExtractor {

	private static final Pattern REGEX_PATTERN_ID = Pattern.compile("([0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12})");

	private String id;

	public CustomerURLPathExtractor(HttpServletRequest request) throws ServletException {
		if (request.getPathInfo() == null) {
			return;
		}

		Matcher matcher = REGEX_PATTERN_ID.matcher(request.getPathInfo());
		if (matcher.find()) {
			id = matcher.group(1);
			return;
		}

		// TODO: trocar por um ValidationError
		throw new ServletException("Invalid URI");
	}

	public String getId() {
		return id;
	}
}