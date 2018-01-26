package cz.kul.snippets.spring._10_overriding.module1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfiguration1 {
	
	@Bean(name = "bean")
	public String module1() {
		System.out.println("module1");
		return "module1";
	}

}
