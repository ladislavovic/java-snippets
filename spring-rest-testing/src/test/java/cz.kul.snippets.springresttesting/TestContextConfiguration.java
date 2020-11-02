package cz.kul.snippets.springresttesting;

import cz.kul.snippets.springresttesting.config.ServletContextConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ServletContextConfiguration.class)
public class TestContextConfiguration {

}
