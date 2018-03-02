package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring.common.Bean1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigHelloWorld {

    @Bean
    public Bean1 fooBean() {
        return new Bean1();
    }
}
