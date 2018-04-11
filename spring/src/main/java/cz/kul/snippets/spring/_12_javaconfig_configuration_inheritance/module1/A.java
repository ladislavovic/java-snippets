package cz.kul.snippets.spring._12_javaconfig_configuration_inheritance.module1;

import cz.kul.snippets.spring.common.Bean1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class A extends B {

    @Bean
    @Override
    public Bean1 bean1() {
        return new Bean1("A");
    }

}
