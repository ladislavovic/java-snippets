package cz.kul.snippets.spring._08_javaconfig;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.kul.snippets.spring.common.Bean1;

@Configuration
public class Config04_collectionAsSpringBean {
	
	@Bean
	List<Bean1> foo() {
		List<Bean1> list = new ArrayList<>();
		list.add(new Bean1("1"));
		list.add(new Bean1("2"));
		list.add(new Bean1("3"));
		return list;
	}

}
