package cz.kul.snippets.springdoc;

import cz.kul.snippets.springdoc.config.ServletContextConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ServletContextConfiguration.class)
public class TestContextConfiguration {

}
