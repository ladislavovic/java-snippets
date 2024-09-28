package cz.kul.snippets.examples_spring_boot;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import jakarta.annotation.PreDestroy;

@TestConfiguration()
public class TestcontainersConfiguration
{
    public static PostgreSQLContainer postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer("postgres:16.2");
        postgresContainer.start();
    }

    @PreDestroy
    public void stopDB() throws Exception
    {
        postgresContainer.stop();
    }

}
