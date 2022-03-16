package cz.kul.snippets.kafka.example01_hw;

import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;

import java.net.InetAddress;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class KafkaHw {

	private static final String TOPIC = "kafka_hw";

	public static void main(String[] args) throws Exception	{

		// Produce
		{
			Stopwatch stopwatch = Stopwatch.createStarted();
			final long NO_OF_RECORDS = 1_000_000;
			produce(TOPIC, NO_OF_RECORDS);
			stopwatch.stop();
			long timeMs = stopwatch.elapsed(TimeUnit.MILLISECONDS);
			double timeS = timeMs / (double) 1000;
			System.out.println("" + NO_OF_RECORDS + " records produced in " + timeMs + " ms (" + NO_OF_RECORDS / timeS + " rec/s)");
		}

		// Consume
		{
			consume(TOPIC, 1000000);
		}

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
		final int valueLength = 100;
		final int maxBeginIndex = sourceLength - valueLength;
		String valueSource = RandomStringUtils.randomAlphabetic(1000);
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

	public static void consume(String topic, long noOfRecords) throws Exception {
		// client creating
		Properties config = new Properties();
		config.put("client.id", InetAddress.getLocalHost().getHostName());
		config.put("group.id", "kafka_hw_group");
		config.put("bootstrap.servers", "localhost:9092");
		config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		config.put("enable.auto.commit", "false");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(config);

		// subscribe to topics and seek to the beginning
		consumer.subscribe(Collections.singletonList(TOPIC), new ConsumerRebalanceListener() {

			@Override
			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
			}

			@Override
			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
				System.out.println("Partition Assigned " + partitions);
				for (TopicPartition tp : partitions) {
					OffsetAndMetadata oam = consumer.committed(tp);
					if (oam != null) {
						System.out.println("Current offset is " + oam.offset());
					} else {
						System.out.println("No committed offsets");
					}
					consumer.seek(tp, 0);
				}
			}
		});

		// consuming
		long noOfConsumed = 0;
		while (noOfConsumed < noOfRecords) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
			System.out.println("Consumed " + records.count() + " messages.");
			noOfConsumed += records.count();
			if (!records.isEmpty()) {
				ConsumerRecord<String, String> firstRecord = records.iterator().next();
				System.out.println("First record: " + firstRecord.key() + ":" + firstRecord.value());
			}

			consumer.commitSync();
			Thread.sleep(1000);
		}

	}




}
