package cz.kul.snippets.spring._08_javaconfig;

import org.springframework.context.annotation.Bean;

import cz.kul.snippets.spring.common.Bean1;

// NOTE: there is not annotation. Otherwise Spring would initialize beans twice
// (in parent and then in children)
public abstract class Config03_abstractConfiguration {
	
	@Bean
	public Bean1 foo() {
		Bean1 b1 = new Bean1();
		b1.setVal("foo");
		return b1;
	}

	@Bean
	public Bean1 bar() {
		Bean1 b1 = new Bean1();
		b1.setVal("bar");
		return b1;
	}

	@Bean
	public abstract Bean1 baz();
	
}
