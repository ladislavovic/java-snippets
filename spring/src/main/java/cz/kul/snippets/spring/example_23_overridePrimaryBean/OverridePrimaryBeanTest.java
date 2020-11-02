package cz.kul.snippets.spring.example_23_overridePrimaryBean;

import cz.kul.snippets.spring.common.SpringTestUtils;
import cz.kul.snippets.spring.example_23_overridePrimaryBean.bean.*;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

public class OverridePrimaryBeanTest {

	@Configuration
	@ComponentScan("cz.kul.snippets.spring.example_23_overridePrimaryBean.bean")
	public static class Config {

		@Bean("ASub1")
		public A override() {
			return new ASub1();
		}

	}


	@Test
	public void test() {
		SpringTestUtils.runInSpring(Config.class, ctx -> {
			B bean = ctx.getBean(B.class);
		});
	}

}
