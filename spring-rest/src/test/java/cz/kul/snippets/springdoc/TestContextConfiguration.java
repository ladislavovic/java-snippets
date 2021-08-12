package cz.kul.snippets.springdoc;

import cz.kul.snippets.springrest.example_01_demo.config.ServletContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@Import({
		ServletContextConfiguration.class
})
public class TestContextConfiguration {

	@Autowired
	private WebApplicationContext wac;

	@Bean
	public MockMvc mockMvc() {
		return MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

}

