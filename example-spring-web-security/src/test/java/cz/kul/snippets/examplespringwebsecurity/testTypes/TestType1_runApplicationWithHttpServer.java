package cz.kul.snippets.examplespringwebsecurity.testTypes;

import cz.kul.snippets.examplespringwebsecurity.ExampleSpringWebSecurityApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * NOTE
 *
 * This type of test starts the whole application and bind it to random local port.
 * Then you can send real HTTP requests to test behaviour.
 *
 */
@ExtendWith(SpringExtension.class) // NOTE this annotation is not needed anymore, because from Spring Boot 2.1 it is part of @SpringBootTest annotation
@SpringBootTest(
		classes = {ExampleSpringWebSecurityApplication.class},
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // It starts the server with a random port
@ActiveProfiles("securityBasic")
public class TestType1_runApplicationWithHttpServer {

	@Value(value="${local.server.port}") // Inject port number the application run on
	private int port;

	@Autowired
	private TestRestTemplate template;

	@Autowired
	private WebApplicationContext context;

	@Test
	public void testAuthetication_shouldFailBecauseThereIsMissingAuthHeader() throws Exception {
		ResponseEntity<Object> entity = template.getForEntity(
				"http://localhost:" + port + "/private/userDetails/001",
				Object.class);
		assertEquals(401, entity.getStatusCodeValue());
	}

	@Test
	public void testAuthetication_shouldFailBecauseOfWrongPassword() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(
				"Authorization",
				"Basic " + Base64.getEncoder().encodeToString("spring:WRONG_PASSWORD".getBytes(StandardCharsets.UTF_8)));

		ResponseEntity<Map> exchange = template.exchange(
				"http://localhost:" + port + "/private/userDetails/001",
				HttpMethod.GET,
				new HttpEntity<>(headers),
				Map.class);

		assertEquals(401, exchange.getStatusCodeValue());
	}

	@Test
	public void testAuthetication_shouldPass() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(
				"Authorization",
				"Basic " + Base64.getEncoder().encodeToString("spring:secret".getBytes(StandardCharsets.UTF_8)));

		ResponseEntity<Map> exchange = template.exchange(
				"http://localhost:" + port + "/private/userDetails/001",
				HttpMethod.GET,
				new HttpEntity<>(headers),
				Map.class);

		assertEquals(200, exchange.getStatusCodeValue());

		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals("Monica", exchange.getBody().get("firstName"))
		);

	}

}
