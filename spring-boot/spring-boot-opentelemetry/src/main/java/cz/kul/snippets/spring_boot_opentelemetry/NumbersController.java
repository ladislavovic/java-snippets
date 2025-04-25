package cz.kul.snippets.spring_boot_opentelemetry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class NumbersController
{

	private static final Logger log = LoggerFactory.getLogger(NumbersController.class);

	@GetMapping("/numbers/integer")
	public int getInteger() {
		log.info("Going to generate random integer");

		int num = (new Random()).nextInt();
		log.info("Generated random integer: {}", num);

		return num;
	}

}
