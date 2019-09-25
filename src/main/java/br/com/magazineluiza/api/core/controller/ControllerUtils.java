package br.com.magazineluiza.api.core.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.jr.ob.JSON;

public class ControllerUtils {

	public static final String APPLICATION_JSON = "application/json";

	public static void writeResponse(Object value, HttpServletResponse response, int statusCode) throws IOException {
		response.setContentType(APPLICATION_JSON);
		response.setStatus(statusCode);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		JSON.std.write(value, response.getOutputStream());
	}
}
