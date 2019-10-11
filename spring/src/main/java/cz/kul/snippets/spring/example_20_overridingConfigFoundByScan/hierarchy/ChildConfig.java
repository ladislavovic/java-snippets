package cz.kul.snippets.spring.example_20_overridingConfigFoundByScan.hierarchy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChildConfig extends ParentConfig {

    @Bean
    @Override
    public String a() {
        return "a_child";
    }

}
