package cz.kul.snippets.kafka.example03_manual_commit;

import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Example03KafkaManualCommit {

	private static final String TOPIC = "kafka_hw";

	private String topicName;

	@Before
	public void prepareTopic() throws Exception {
		topicName = RandomStringUtils.randomAlphabetic(5);
		produce(topicName, 100);
	}

	private void produce(String topic, long noOfRecords) throws Exception {
		Properties config = new Properties();
		config.put("client.id", InetAddress.getLocalHost().getHostName());
		config.put("bootstrap.servers", "localhost:9092");
		config.put("acks", "all");
		config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		KafkaProducer<String, String> producer = new KafkaProducer<>(config);

		for (long i = 0; i < noOfRecords; i++) {
			String key = Long.toString(i);
			String value = "value_" + key;
			ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
			producer.send(record);
		}
		producer.flush();
	}

	/*
	 * Poll does not does not poll from the commit
	 *
	 *
	 */

	@Test
	public void consumeWithManualCommit() throws UnknownHostException {
		final Properties props = new Properties();
		props.put(ConsumerConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka_hw_group");
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

		try (Consumer<String, String> consumer = new KafkaConsumer<>(props)) {
			consumer.subscribe(Arrays.asList(topicName));

			ConsumerRecords<String, String> records;
			while (true) {
				records = consumer.poll(Duration.ofMillis(100));
				if (!records.isEmpty()) {
					break;
				}
			}
			Assert.assertEquals("0", records.iterator().next().key());

			while (true) {
				records = consumer.poll(Duration.ofMillis(100));
				if (!records.isEmpty()) {
					break;
				}
			}
			Assert.assertEquals("0", records.iterator().next().key());
		}
	}

}
