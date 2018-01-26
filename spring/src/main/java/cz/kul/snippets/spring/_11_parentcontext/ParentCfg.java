package cz.kul.snippets.spring._11_parentcontext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.kul.snippets.spring.common.Bean1;

@Configuration
public class ParentCfg {
	
	@Bean
	public Bean1 bean1() {
		return new Bean1("parent");
	}

}
