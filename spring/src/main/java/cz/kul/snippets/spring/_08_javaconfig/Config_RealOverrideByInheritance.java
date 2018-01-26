package cz.kul.snippets.spring._08_javaconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("cz.kul.snippets.spring._08_javaconfig.scan2")
public class Config_RealOverrideByInheritance extends Config_AbstractOverrideByInheritance {

}
