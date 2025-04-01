package cz.kul.snippets.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;
	private final Receiver receiver;

	public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
		this.receiver = receiver;
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Sending message...");
//		rabbitTemplate.convertAndSend(
//			MessagingRabbitmqApplication.topicExchangeName,
//			"foo.bar.123",
//			"Hello from RabbitMQ!" // you can send any object, it is converted to byte[], because AMQP is a binary protocol
//		);


		rabbitTemplate.convertAndSend(
			MessagingRabbitmqApplication.topicExchangeName,
			"user.1",
			"name: Monica"
		);

		rabbitTemplate.convertAndSend(
			MessagingRabbitmqApplication.topicExchangeName,
			"user.2",
			"name: Rachel"
		);

		rabbitTemplate.convertAndSend(
			MessagingRabbitmqApplication.topicExchangeName,
			"order.1",
			"sum: 10.00 USD"
		);


		Thread.sleep(3000);
	}

}
