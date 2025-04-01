package cz.kul.snippets.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver implements MessageListener
{

	@Override
	public void onMessage(final Message message)
	{
		System.out.println("Raw message received: " + message);
	}

	// it is for the adapter
	public void receiveMessage(String message)
	{
		System.out.println("Received <" + message + ">");
	}

}
