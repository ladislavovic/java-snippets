package cz.kul.snippets.kafka.example02_topic_with_more_partitions;

import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Example02 {

	private static final String TOPIC_NAME = "topic_example02a";

	public static class TopicCreator {
		public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
			Properties properties = new Properties();
			properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
			KafkaFuture<Void> future;
			try (Admin admin = Admin.create(properties)) {
			  String topicName = TOPIC_NAME;
				int partitions = 3;
				short replicationFactor = 1;
				NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
				CreateTopicsResult result = admin.createTopics(Collections.singleton(newTopic));
				future = result.values().get(topicName);
			}
			Void unused = future.get(10, TimeUnit.SECONDS);
		}
	}

	/*
	 NOTE: it publishes to all partitions out of the box. I did not configure anything. Probably
	       you can configure it somehow.
	 */
	public static class Producer {
		public static void main(String[] args) throws Exception {
			final long NO_OF_RECORDS = 10_000;
			produce(TOPIC_NAME, NO_OF_RECORDS);
		}
		public static void produce(String topic, long noOfRecords) throws Exception {
			Properties config = new Properties();
			config.put("client.id", InetAddress.getLocalHost().getHostName());
			config.put("bootstrap.servers", "localhost:9092");
			config.put("acks", "all");
			config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
			config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
			KafkaProducer<String, String> producer = new KafkaProducer<>(config);

			final int sourceLength = 1000;
			final int valueLength = 10;
			final int maxBeginIndex = sourceLength - valueLength;
			String valueSource = RandomStringUtils.randomAlphabetic(sourceLength);
			Random random = new Random();

			for (long i = 0; i < noOfRecords; i++) {
				int beginIndex = random.nextInt(maxBeginIndex);
				String value = valueSource.substring(beginIndex, beginIndex + valueLength);
				String key = Long.toString(i);
				ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
				producer.send(record);
			}
			producer.flush();
		}
	}

	/*
	  NOTE: it consume from all partitions out of the box. Records in batch are all from the same partition,
	        but the batches are randomly pulled from all partitions by rotation.
	 */
	public static class Consumer {
		public static void main(String[] args) throws Exception {
			consume(TOPIC_NAME, 200);
		}
		public static void consume(String topic, int limit) throws UnknownHostException {
			String rnd = RandomStringUtils.randomAlphabetic(5).toUpperCase(Locale.ROOT);
			final Properties props = new Properties();
//			props.put(ConsumerConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
			props.put(ConsumerConfig.CLIENT_ID_CONFIG, "kafka_example02_client_id_" + rnd);
			props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka_example02_group_" + rnd);
			props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
			props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
			props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
			props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
			props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 32);

			// Add additional required properties for this consumer app
			final org.apache.kafka.clients.consumer.Consumer<String, String> consumer = new KafkaConsumer<>(props);
			consumer.subscribe(Arrays.asList(topic));

			try {
				int counter = 0;
				L0: while (true) {
					ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
					System.out.println("\nConsuming another batch. Size=" + records.count());
					for (ConsumerRecord<String, String> record : records) {
						String msg = String.format(
								"Event consumed. key=%s, value=%s, partition=%s, offset=%s",
								record.key(),
								record.value(),
								record.partition(),
								record.offset());
						System.out.println(msg);
						if (counter++ > limit) {
							break L0;
						}
					}
				}
			} finally {
				consumer.close();
			}
		}
	}




}
