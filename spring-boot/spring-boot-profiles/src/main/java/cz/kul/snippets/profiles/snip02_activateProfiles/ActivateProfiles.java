package cz.kul.snippets.profiles.snip02_activateProfiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.kul.snippets.Console.logAndAssert;

@SpringBootApplication
public class ActivateProfiles implements ApplicationRunner
{
	private static final Logger log = LoggerFactory.getLogger(ActivateProfiles.class);

	@Autowired
	ApplicationContext applicationContext;

	public static void main(String[] args)
	{
		SpringApplication.run(ActivateProfiles.class, "--spring.profiles.active=prod,aws");
	}

	@Override
	public void run(final ApplicationArguments args) throws Exception
	{
		var environment = applicationContext.getEnvironment();

		Set<String> activeProfiles = Arrays.stream(environment.getActiveProfiles()).collect(Collectors.toSet());
		logAndAssert(
			"You can use property \"spring.profiles.active\" to activate profiles",
			"ActiveProfiles",
			activeProfiles,
			Set.of("prod", "aws"),
			log
		);

		boolean theProfileWithNameDefaultIsActive = environment.acceptsProfiles("default");
		logAndAssert(
			"When you activate any profiles, the default profile is not active anymore",
			"theProfileWithNameDefaultIsActive",
			theProfileWithNameDefaultIsActive,
			false,
			log
		);

	}

}
