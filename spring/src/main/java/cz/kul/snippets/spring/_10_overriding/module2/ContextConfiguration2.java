package cz.kul.snippets.spring._10_overriding.module2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfiguration2 {
	
	@Bean(name = "bean")
	public String module2() {
		System.out.println("module2");
		return "module2";
	}

}
