package cz.kul.snippets.spring._10_overriding.componentScanModule3;

import cz.kul.snippets.spring.common.Bean1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("cz.kul.snippets.spring._10_overriding.componentScanModule5")
public class Importee1ConfigByComponentSccan2 {

    @Bean
    Bean1 bean1() {
        return new Bean1("module3");
    }

}
