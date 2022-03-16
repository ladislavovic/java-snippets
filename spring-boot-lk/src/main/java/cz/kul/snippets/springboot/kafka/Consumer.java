package cz.kul.snippets.springboot.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer {

	private final Logger logger = LoggerFactory.getLogger(Consumer.class);

	@KafkaListener(topics = "users", groupId = "group_id")
	public void consume(ConsumerRecord<Object, Object> record) throws IOException {
		logger.info(String.format("#### -> Consumed message -> %s", record));
	}

//	public void listenGroupFoo(ConsumerRecord<Object, Object> record) {
//		System.out.println("Received Message in group foo: " + record);
//	}

}
