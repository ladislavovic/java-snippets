package cz.kul.snippets.spring.example_20_overridingConfigFoundByScan.hierarchy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ParentConfig.class)
public class ImportingConf {

    @Bean
    public String a() {
        return "a_importing";
    }
    
    
    
}
