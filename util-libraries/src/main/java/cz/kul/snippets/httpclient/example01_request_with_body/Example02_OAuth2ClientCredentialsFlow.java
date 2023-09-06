package cz.kul.snippets.httpclient.example01_request_with_body;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Example02_OAuth2ClientCredentialsFlow {

	/**
	 * This test first get token from the issuer and then
	 * request the resource with the token in Authentication header.
	 */
	@Test
	public void getResourceWithOauth2Auth() throws Exception {

		try (CloseableHttpClient client = HttpClients.createDefault()) {

			String token = getNewToken(
					"http://uaa:9999/realms/cross/protocol/openid-connect/token",
					"swagger-ui",
					"KmIdlydXaB9QX8S5UJ9lpPtfio47zv3D",
					client);

			System.out.println("\nToken: " + token);

			String resource = getResource("http://localhost:8888/v1/linkType/OPTICAL_FIBER", token, client);
			System.out.println("\nResource: " + resource);
		}
	}

	private String getResource(
			String resourceUrl,
			String token,
			HttpClient httpClient) throws IOException {

		HttpUriRequest request = RequestBuilder
				.get(resourceUrl)
				.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.build();

		try {
			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} finally {
			request.abort();
		}

	}

	private String getNewToken(
			String tokenUrl,
			String user,
			String password,
			HttpClient httpClient) {

		// Create credentials provider
		URI tokenUri;
		try {
			tokenUri = new URI(tokenUrl);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		final HttpHost target = new HttpHost(tokenUri.getHost(), tokenUri.getPort(), tokenUri.getScheme());
		final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(
				new AuthScope(target),
				new UsernamePasswordCredentials(user, password));

		// Create AuthCache instance
		final AuthCache authCache = new BasicAuthCache();
		authCache.put(target, new BasicScheme());

		// Execution context
		final HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credentialsProvider);
		context.setAuthCache(authCache);

		// Get a token
		List<BasicNameValuePair> formData = new ArrayList<>();
		formData.add(new BasicNameValuePair("grant_type", "client_credentials"));
		HttpUriRequest tokenRequest = RequestBuilder.create(HttpPost.METHOD_NAME)
				.setUri(tokenUri)
				.setEntity(new UrlEncodedFormEntity(formData, StandardCharsets.UTF_8))
				.build();

		try {
			HttpResponse response = httpClient.execute(target, tokenRequest, context);
			int statusCode = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK != statusCode) {
				StringBuilder errMsg = new StringBuilder();
				errMsg.append("Could not get an OAuth2 authentication token.");
				errMsg.append("StatusCode=" + statusCode);

				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String entityStr = EntityUtils.toString(entity);
					if (entityStr != null && !entityStr.isEmpty()) {
						errMsg.append(", Details='" + entityStr + "'");
					}
				}
				throw new RuntimeException(errMsg.toString());
			}

			HttpEntity entity1 = response.getEntity();
			ObjectMapper objectMapper = new ObjectMapper();
			HashMap hashMap = objectMapper.readValue(EntityUtils.toString(entity1), HashMap.class);
			return (String) hashMap.get("access_token");

		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			// weird, but this is the method that releases resources, including the connection
			tokenRequest.abort();
		}

	}

}
