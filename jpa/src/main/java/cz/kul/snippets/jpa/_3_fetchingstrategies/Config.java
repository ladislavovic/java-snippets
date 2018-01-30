package cz.kul.snippets.jpa._3_fetchingstrategies;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("cz.kul.snippets.jpa._3_fetchingstrategies")
@PropertySource({ "classpath:/cz/kul/snippets/jpa/_3_fetchingstrategies/override.properties" })
public class Config {

}
