package cz.kul.snippets.spring._10_overriding;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
//@ComponentScan({
//	"cz.kul.snippets.spring._10_overriding.module2"
//})
@Import({
	Module1ContextConfiguration.class,
	Module2ContextConfiguration.class
})
public class ContextConfiguration {

}
