package cz.kul.snippets.spring.example_20_overridingConfigFoundByScan.hierarchy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParentConfig {
    
    @Bean
    public String a() {
        return "a_parent";
    }
    
}
