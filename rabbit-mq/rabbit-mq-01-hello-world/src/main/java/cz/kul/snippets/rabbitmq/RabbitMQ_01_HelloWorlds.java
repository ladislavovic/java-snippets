package cz.kul.snippets.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sound.midi.Receiver;

/*

It just creates an queue, exchange, binding and send two messages. The listener receive them. Done.

 */
@SpringBootApplication
public class RabbitMQ_01_HelloWorlds
{
	static final String topicExchangeName = "spring-boot-exchange";

	static final String queueName = "spring-boot";

	// Message queue.
	// * The consumer consume messages from it.
	// * the producer do NOT send messages directly to the queue, but to the Exchange
	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	// Exchange is responsible for routing messages to queues
	@Bean
	TopicExchange exchange() {
		return new TopicExchange(topicExchangeName);
	}

	// A link between exchange and queue. It just configures the message routing
	@Bean
	Binding binding(
		@Qualifier("queue") Queue queue,
		TopicExchange exchange
	) {
		return BindingBuilder
			.bind(queue)
			.to(exchange)
			.with("user.#"); // # is a wildcard, see below

		/*
		    # is a wildcard that matches one or more words. So all messages started with "user." will be routed to this queue. Examples:
		        user.config
		        user.config.address
		        user.config.address.street

		    Words are separated by dot character. (.)

		    There is also * wildcard. That matches just one word. So the routing key "user.*" would route
		        user.config
		        user.permissions
		    But not route
		        user.config.address
		 */
	}

	// TODO ???
	@Bean
	SimpleMessageListenerContainer container(
		ConnectionFactory connectionFactory
	)
	{
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);

		// implementation of MessageListener
		container.setMessageListener(message -> System.out.println("Raw message received: " + message));
		return container;
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(RabbitMQ_01_HelloWorlds.class, args).close();
	}

}
