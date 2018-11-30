package cz.kul.snippets.spring._08_javaconfig.module1;

import cz.kul.snippets.spring.common.Bean1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class C extends B {

    @Bean
    @Override
    public Bean1 bean1() {
        return new Bean1("C");
    }

}
