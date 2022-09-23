package cz.kul.snippets.springboot;

import cz.kul.snippets.springboot.externalLib.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
