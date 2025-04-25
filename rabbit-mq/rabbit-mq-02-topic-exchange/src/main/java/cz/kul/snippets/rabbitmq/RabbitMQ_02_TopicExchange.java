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
public class RabbitMQ_02_TopicExchange
{
	static final String mainExchangeName = "my_exhange";

	static final String userQueueName = "user_queue";

	static final String orderQueueName = "order_queue";

	@Bean
	Queue userQueue()
	{
		return new Queue(userQueueName, false);
	}

	@Bean
	Queue orderQueue()
	{
		return new Queue(orderQueueName, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(mainExchangeName);
	}

	@Bean
	Binding usersBinding(
		@Qualifier("userQueue") Queue usersQueue,
		TopicExchange exchange
	)
	{
		return BindingBuilder
			.bind(usersQueue)
			.to(exchange)
			.with("user.#");
	}

	@Bean
	Binding ordersBinding(
		@Qualifier("orderQueue") Queue ordersQueue,
		TopicExchange exchange
	)
	{
		return BindingBuilder
			.bind(ordersQueue)
			.to(exchange)
			.with("order.#");
	}

	@Bean
	SimpleMessageListenerContainer usersQueueListenerContainer(
		ConnectionFactory connectionFactory
	)
	{
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(userQueueName);
		container.setMessageListener(message -> System.out.println("Message from users queue received: " + message));
		return container;
	}

	@Bean
	SimpleMessageListenerContainer ordersQueueListenerContainer(
		ConnectionFactory connectionFactory
	)
	{
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(orderQueueName);
		container.setMessageListener(message -> System.out.println("Message from orders queue received: " + message));
		return container;
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(RabbitMQ_02_TopicExchange.class, args).close();
	}

}
