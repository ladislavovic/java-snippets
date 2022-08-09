package cz.kul.snippets.springboot.security_02_customAuthenticationProvider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(
		classes = {ApplicationSecurity02.class},
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

	@Autowired
	private TestRestTemplate template;

	@Test
	public void customProviderIsUsed() throws Exception {
		String body = template
				.withBasicAuth("admin", "foo")
				.getForEntity("/hello", String.class)
				.getBody();
		assertEquals("hello", body);
	}

}