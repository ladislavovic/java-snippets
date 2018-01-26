package cz.kul.snippets.spring._10_overriding;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
	"cz.kul.snippets.spring._10_overriding.module1"
})
public class Module1ContextConfiguration {
	
}
