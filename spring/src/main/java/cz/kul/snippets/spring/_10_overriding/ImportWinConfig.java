package cz.kul.snippets.spring._10_overriding;

import cz.kul.snippets.spring.common.Bean1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ImportWinConfig_Imported.class
})
public class ImportWinConfig {

    @Bean
    public Bean1 foo() {
        return new Bean1("config");
    }

}
