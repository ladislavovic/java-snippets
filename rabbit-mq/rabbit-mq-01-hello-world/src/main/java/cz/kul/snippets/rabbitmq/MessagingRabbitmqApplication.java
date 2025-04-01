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

@SpringBootApplication
public class MessagingRabbitmqApplication {

	static final String topicExchangeName = "spring-boot-exchange";

	static final String queueName = "spring-boot";

	static final String queueName2 = "spring-boot-2";

	// Message queue.
	// * The consumer consume messages from it.
	// * the producer do NOT send messages directly to the queue, but to the Exchange
	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	Queue queue2() {
		return new Queue(queueName2, false);
	}

	// Exchange is responsible for routing messages to queues
	@Bean
	TopicExchange exchange() {
		return new TopicExchange(topicExchangeName);
	}

	// A link between exchange and queue. It just configure messages routing
	@Bean
	Binding binding(@Qualifier("queue") Queue queue, TopicExchange exchange) {
		return BindingBuilder
			.bind(queue)
			.to(exchange)
			.with("user.#");
	}

	@Bean
	Binding binding2(@Qualifier("queue2") Queue queue, TopicExchange exchange) {
		return BindingBuilder
			.bind(queue)
			.to(exchange)
			.with("order.#");
	}

	@Bean
	SimpleMessageListenerContainer container(
		ConnectionFactory connectionFactory,
		Receiver receiver
	)
	{
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName, queueName2);
		container.setMessageListener(receiver);
		return container;
	}

	// The message listener
//	@Bean
//	MessageListenerAdapter listenerAdapter(Receiver receiver) {
//		return new MessageListenerAdapter(receiver, "receiveMessage");
//	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(MessagingRabbitmqApplication.class, args).close();
	}

}
