package cz.kul.snippets.spring._10_overriding;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.kul.snippets.spring.common.Bean1;

@Configuration
public class BaseConfig {

	@Bean(name = {"base_foo", "foo"})
	public Bean1 foo() {
		return new Bean1("base");
	}

}