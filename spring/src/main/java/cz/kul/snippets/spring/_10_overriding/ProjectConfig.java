package cz.kul.snippets.spring._10_overriding;

import org.springframework.context.annotation.Configuration;

import cz.kul.snippets.spring.common.Bean1;

@Configuration
public class ProjectConfig extends AbstractProjectConfig {
	
	@Override
	public Bean1 foo() {
		return new Bean1("project");
	}

}