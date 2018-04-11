package cz.kul.snippets.spring._10_overriding.componentScanModule7;

import cz.kul.snippets.spring.common.Bean1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParentInnerConfigByComponentScan {

    @Bean
    Bean1 bean1() {
        return new Bean1("module7");
    }

}
