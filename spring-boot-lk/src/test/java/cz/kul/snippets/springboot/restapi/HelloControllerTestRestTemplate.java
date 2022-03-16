package cz.kul.snippets.springboot.restapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

// This test does real HTTP requests

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerTestRestTemplate {

	@LocalServerPort
	private int port;

	private String url;

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	public void setUp() {
		url = String.format("http://localhost:%d/hello", port);
	}

	@Test
	public void greetingShouldReturnDefaultMessage() {
		assertThat(this.restTemplate.getForObject(url, String.class)).contains("Hello World!");
	}
}
