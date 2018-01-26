package cz.kul.snippets.spring._10_overriding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import cz.kul.snippets.spring.common.Bean1;

public abstract class AbstractProjectConfig {
	
	@Autowired
	@Qualifier("base_foo")
	Bean1 base;
	
	@Bean(name = {"project_foo", "foo"})
	public Bean1 foo() {
		return base;
	}
	
}