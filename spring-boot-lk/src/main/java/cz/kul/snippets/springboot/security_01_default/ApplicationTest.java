package cz.kul.snippets.springboot.security_01_default;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
		classes = {Application.class},
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = {"spring.security.user.password=tree"})
public class ApplicationTest {

	@Autowired
	private TestRestTemplate template;

	@Test
	public void theLoginFormIsShown() throws Exception {
		ResponseEntity<String> response = template.getForEntity("/", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.toString().contains("Please sign in"));
	}

	@Test
	public void youMustAuthenticate() throws Exception {
		ResponseEntity<String> response1 = template.getForEntity("/hello", String.class);
		ResponseEntity<String> response2 = template.withBasicAuth("user", "tree").getForEntity("/hello", String.class);
		assertNotEquals("hello", response1.getBody());
		assertEquals("hello", response2.getBody());
	}

}