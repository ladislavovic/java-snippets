package cz.kul.snippets.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;

	public Runner(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Sending 1 user message and 1 orders message ...");

		rabbitTemplate.convertAndSend(
			RabbitMQ_02_TopicExchange.mainExchangeName,
			"user.1",
			"name: Monica"
		);

		rabbitTemplate.convertAndSend(
			RabbitMQ_02_TopicExchange.mainExchangeName,
			"order.1",
			"sum: 1000"
		);

		Thread.sleep(3000);
	}

}
