package cz.kul.snippets.springboot.security_03_moreProviders;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(
		classes = {ApplicationSecurity03.class},
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

	@Autowired
	private TestRestTemplate template;

	@Test
	public void itTryProvidersOneByOne() throws Exception {
		String body;
		body = template
				.withBasicAuth("admin", "foo")
				.getForEntity("/hello", String.class)
				.getBody();
		assertEquals("hello", body);

		body = template
				.withBasicAuth("user1", "foo")
				.getForEntity("/hello", String.class)
				.getBody();
		assertEquals("hello", body);

		body = template
				.withBasicAuth("user2", "foo")
				.getForEntity("/hello", String.class)
				.getBody();
		assertEquals("hello", body);

		body = template
				.withBasicAuth("user3", "foo")
				.getForEntity("/hello", String.class)
				.getBody();
		assertNotEquals("hello", body);
	}

}