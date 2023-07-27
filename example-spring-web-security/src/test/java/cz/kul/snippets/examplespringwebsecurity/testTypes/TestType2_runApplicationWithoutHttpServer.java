package cz.kul.snippets.examplespringwebsecurity.testTypes;

import cz.kul.snippets.examplespringwebsecurity.ExampleSpringWebSecurityApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * NOTE
 *
 * This type of test starts the application but without the web server.
 *
 * Then you can test your application by MockMvc. The whole spring request processing stack is called
 * like you use real HTTP request.
 *
 * The advantage is it is faster, because you do not have start HTTP server.
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ExampleSpringWebSecurityApplication.class})
@ActiveProfiles("securityBasic")
public class TestType2_runApplicationWithoutHttpServer {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) // NOTE: use that to apply security to your mocked request. Without that the security is not used.
				.build();
	}

	@Test
	public void testAuthetication_shouldFailBecauseThereIsMissingAuthHeader() throws Exception {
		mvc.perform(MockMvcRequestBuilders
						.get("/private/userDetails/001")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(401));
	}

	@Test
	public void testAuthetication_shouldFailBecauseOfWrongPassword() throws Exception {
		mvc.perform(MockMvcRequestBuilders
						.get("/private/userDetails/001")
						.header("Authorization", "Basic " + Base64.getEncoder().encodeToString("spring:WRONG_PASSWORD".getBytes(StandardCharsets.UTF_8)))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(401));
	}

	@Test
	public void testAuthetication_shouldPass() throws Exception {
		ResultActions result = mvc.perform(MockMvcRequestBuilders
						.get("/private/userDetails/001")
						.header("Authorization", "Basic " + Base64.getEncoder().encodeToString("spring:secret".getBytes(StandardCharsets.UTF_8)))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(mvcResult -> mvcResult.getResponse().getContentAsString().contains("Monica"));
	}

}
