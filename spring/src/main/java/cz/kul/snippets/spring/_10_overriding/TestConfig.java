package cz.kul.snippets.spring._10_overriding;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ 
	BaseConfig.class, 
	ProjectConfig.class
})
public class TestConfig {
}