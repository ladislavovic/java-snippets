package cz.kul.snippets.spring._10_overriding.componentScanModule6;

import cz.kul.snippets.spring.common.Bean1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootInner1ConfigByComponentScan {

    @Bean
    Bean1 bean1() {
        return new Bean1("module6");
    }

}
