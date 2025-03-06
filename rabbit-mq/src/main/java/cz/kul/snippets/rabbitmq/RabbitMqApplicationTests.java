package cz.kul.snippets.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

//@Import(TestcontainersConfiguration.class)
@Testcontainers
@SpringBootTest
class RabbitMqApplicationTests
{
	@Container
	static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer();


	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Test
	void testRabbitMqMessage()
	{
		rabbitTemplate.convertAndSend(RabbitMqApplication.QUEUE_NAME, "Test Message");

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			String received = (String) rabbitTemplate.receiveAndConvert(RabbitMqApplication.QUEUE_NAME);
			assert received != null && received.equals("Test Message");
		});
	}

	@Test
	void contextLoads()
	{
	}

}
