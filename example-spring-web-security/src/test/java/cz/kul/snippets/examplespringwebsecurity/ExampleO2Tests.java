package cz.kul.snippets.examplespringwebsecurity;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TODO I should run keycloak by testcontainers. See:
 *   https://www.baeldung.com/spring-boot-keycloak-integration-testing
 *   https://www.keycloak.org/server/importExport
 *
 */
@SpringBootTest(
		classes = {ExampleSpringWebSecurityApplication.class},
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("example02")
public class ExampleO2Tests {

	@Value(value="${local.server.port}") // Inject port number the application run on
	private int port;

	@Autowired
	private TestRestTemplate template;

	@Autowired
	private WebApplicationContext context;

	@Test
	public void test() throws Exception {

		//
		// 01 Get token from Keycloak
		//
		String accessToken;
		{
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add(
					"Authorization",
					"Basic " + Base64.getEncoder().encodeToString("swagger-ui:kICkuAQpPEAlHEdmB3OOM5CjzSY1SpbM".getBytes(StandardCharsets.UTF_8)));

			MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
			requestBody.add("grant_type", "client_credentials");

			ResponseEntity<Map> response = template.exchange(
					"http://localhost:9999/realms/cross/protocol/openid-connect/token",
					HttpMethod.POST,
					new HttpEntity<>(requestBody, headers),
					Map.class);
			assertEquals(200, response.getStatusCodeValue());
			assertNotNull(response.getBody());
			accessToken = (String) response.getBody().get("access_token");
			assertNotNull(accessToken);
		}

		//
		// 02 Get security context details
		//
		{
			HttpHeaders headers = new HttpHeaders();
			headers.add(
					"Authorization",
					"Bearer " + accessToken);

			ResponseEntity<Map> response = template.exchange(
					"http://localhost:" + port + "/private/securityContextDetails",
					HttpMethod.GET,
					new HttpEntity<>(headers),
					Map.class);

			assertEquals(200, response.getStatusCodeValue());
			Map body = response.getBody();

			String jsonString = new ObjectMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(body);
			System.out.println("Response Body: \n" + jsonString);

			assertTrue(((String) body.get("principalClassName")).contains("UserDetailsImpl"));

			Collection<String> authorities = (Collection<String>) body.get("authorities");
			authorities.contains("CROSS_ADMINS");
			authorities.contains("CROSS_USERS");

		}


	}

}
