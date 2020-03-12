package cz.kul.snippets.junit.example03_springIntegration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Cfg {
    
    @Bean
    public String bean1() {
        return "foo";
    }
    
}
