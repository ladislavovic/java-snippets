package cz.kul.snippets.springdoc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("cz.kul.snippets.springdoc.app")
@EnableWebMvc
public class ServletContextConfiguration implements WebMvcConfigurer {

}
