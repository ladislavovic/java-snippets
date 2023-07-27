package cz.kul.snippets.examplespringwebsecurity.testTypes;

import cz.kul.snippets.examplespringwebsecurity.common.UserController;
import cz.kul.snippets.examplespringwebsecurity.common.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * NOTE
 *
 * This type of test does not initialize the whole application context. It initializes only web layer
 * (controllers, controllers advices, MockMvc bean, ...). You can also select which controllers you
 * want to initialize.
 *
 * ### Test Slices
 * The annotations @...Test are called Test Slices annotation. We have for example @WebMvcTest, @WebFluxTest,
 * @DataJdbcTest, ... They can be used to test slices of your application.
 * They auto-configure your application. Here is the list of slice annotations and they auto configurations:
 * https://docs.spring.io/spring-boot/docs/current/reference/html/test-auto-configuration.html
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ActiveProfiles("securityBasic")
public class TestType3_runOnlyWebLayer {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService service; // NOTE must create mock of the UserService otherwise is not able to initialize context

	@WithMockUser(value = "aaa") // NOTE you can mock the user this way
	@Test
	public void testGetAPrivateResource() throws Exception {
		// NOTE: notice the security headers does not have to be here
		ResultActions result = mvc.perform(MockMvcRequestBuilders
						.get("/private/userDetails/001")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(mvcResult -> mvcResult.getResponse().getContentAsString().contains("Monica"));
	}

}
