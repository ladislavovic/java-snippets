package cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule9;

import cz.kul.snippets.spring.common.Bean1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParentOfRootInner1ConfigByComponentScan {

    @Bean
    Bean1 bean1() {
        return new Bean1("module9");
    }

}
