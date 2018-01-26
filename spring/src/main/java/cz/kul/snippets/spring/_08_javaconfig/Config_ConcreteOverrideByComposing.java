package cz.kul.snippets.spring._08_javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	Config_GeneralOverrideByComposing.class
})
@ComponentScan("cz.kul.snippets.spring._08_javaconfig.scan2")
public class Config_ConcreteOverrideByComposing {

	@Bean
	public Bean1 foo() {
		Bean1 bean = new Bean1();
		bean.setValue("concrete");
		return bean;
	}
	
}
