package cz.kul.snippets.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sound.midi.Receiver;

@Component
public class Runner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;

	public Runner(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Sending 2 messages ...");

		rabbitTemplate.convertAndSend(
			RabbitMQ_01_HelloWorlds.topicExchangeName,
			"user.1",
			"name: Monica"
		);

		rabbitTemplate.convertAndSend(
			RabbitMQ_01_HelloWorlds.topicExchangeName,
			"user.2",
			"name: Rachel"
		);

		Thread.sleep(3000);
	}

}
