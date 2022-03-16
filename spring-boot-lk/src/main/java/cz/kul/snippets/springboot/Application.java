package cz.kul.snippets.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication
//public class Application {
//
//	public static void main(String[] args) {
//		SpringApplication.run(Application.class, args);
//	}
//
//}

@EnableJpaRepositories("cz.kul.snippets.springboot.persistence")
@EntityScan("cz.kul.snippets.springboot.persistence")
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
