package br.com.magazineluiza.api.product;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductClient {

	private static final Logger logger = LogManager.getLogger();

	private static final String RESOURCE_BUNDLE_NAME = "application";
	private static final String URL_KEY = "luiza.labs.base.url";
	private static final String PRODUCT_RESOURCE = "product";

	private ResourceBundle applicationProperties;

	public ProductClient() {
		this.applicationProperties = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
	}

	public boolean exists(String id) {
		try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
			String url = applicationProperties.getString(URL_KEY) + "/" + PRODUCT_RESOURCE + "/" + id;
			HttpGet httpGet = new HttpGet(url);
			logger.debug("Request: {}", httpGet.getRequestLine());

			ResponseHandler<String> responseHandler = response -> {
				int status = response.getStatusLine().getStatusCode();
				if (status == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					return entity == null ? null : EntityUtils.toString(entity, StandardCharsets.UTF_8);
				} else {
					throw new ClientProtocolException(String.valueOf(status));
				}
			};

			try {
				String payload = httpclient.execute(httpGet, responseHandler);
				logger.debug("Response: {}", payload);
				return true;
			} catch (ClientProtocolException e) {
				if (String.valueOf(HttpStatus.SC_NOT_FOUND).equals(e.getMessage())) {
					return false;
				} else {
					logger.error(e);
					throw e;
				}
			}
		} catch (IOException e) {
			return false;
		}
	}
}