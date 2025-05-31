package cz.kul.snippets.profiles.snip01_DefaultProfile;

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
import java.util.Set;
import java.util.stream.Collectors;

import static cz.kul.snippets.Console.logAndAssert;

@SpringBootApplication
public class DefaultProfile implements ApplicationRunner
{
	private static final Logger log = LoggerFactory.getLogger(DefaultProfile.class);

	private static final String BEAN_FOR_DEFAULT_PROFILE = "beanForDefaultProfile";
	private static final String BEAN_FOR_PROFILE_1 = "beanForProfile1";

	@Autowired
	ApplicationContext applicationContext;

	@Bean(name = BEAN_FOR_DEFAULT_PROFILE)
	@Profile("default")
	String beanForDefaultProfile()
	{
		return "foo";
	}

	@Bean(name = BEAN_FOR_PROFILE_1)
	@Profile("profile1")
	String beanForProfile1()
	{
		return "bar";
	}

	public static void main(String[] args)
	{
		SpringApplication.run(DefaultProfile.class, args);
	}

	@Override
	public void run(final ApplicationArguments args) throws Exception
	{
		var environment = applicationContext.getEnvironment();

		Set<String> defaultProfiles = Arrays.stream(environment.getDefaultProfiles()).collect(Collectors.toSet());

		logAndAssert(
			"If no profile is enabled, a default profile is enabled (you can change the default profile name by the property spring.profiles.default)",
			"DefaultProfiles",
			defaultProfiles,
			Set.of("default"),
			log
		);

		Set<String> activeProfiles = Arrays.stream(environment.getActiveProfiles()).collect(Collectors.toSet());
		logAndAssert(
			"Be careful, the default profile is not among \"getActiveProfiles()\"",
			"ActiveProfiles",
			activeProfiles,
			Set.of(),
			log
		);

		boolean theProfileWithNameDefaultIsActive = environment.acceptsProfiles("default");
		logAndAssert(
			"You can use default profile name for testing if it is active as any other profile",
			"theProfileWithNameDefaultIsActive",
			theProfileWithNameDefaultIsActive,
			true,
			log
		);

		Map<String, Boolean> beansPresence = Map.of(
			BEAN_FOR_DEFAULT_PROFILE, applicationContext.containsBean(BEAN_FOR_DEFAULT_PROFILE),
			BEAN_FOR_PROFILE_1, applicationContext.containsBean(BEAN_FOR_PROFILE_1)
		);
		logAndAssert(
			"You can use default profile for conditional bean creating",
			"beansPresence",
			beansPresence,
			Map.of(
				BEAN_FOR_DEFAULT_PROFILE, true,
				BEAN_FOR_PROFILE_1, false
			),
			log
		);
	}

}
