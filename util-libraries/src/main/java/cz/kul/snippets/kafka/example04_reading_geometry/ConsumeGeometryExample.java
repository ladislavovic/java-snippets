package cz.kul.snippets.kafka.example04_reading_geometry;

import org.apache.avro.generic.GenericRecord;
import org.apache.commons.codec.binary.Hex;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumeGeometryExample {

  public static void main(String[] args) throws Exception {
    final String TOPIC = "cross.cdc.v1.node_geometry";
    final int PROCESSED_RECORDS_LIMIT = 3;

    try (Consumer<GenericRecord, GenericRecord> consumer = createConsumerAndSubscribe(TOPIC)) {
      pollAndProcess(consumer, PROCESSED_RECORDS_LIMIT);
    }
  }

  public static void pollAndProcess(Consumer<GenericRecord, GenericRecord> consumer, int limit) {
    int counter = 0;
    while (true) {
      ConsumerRecords<GenericRecord, GenericRecord> records = consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<GenericRecord, GenericRecord> record : records) {
        GenericRecord recKey = record.key();
        Object nodeGeometryId = recKey.get("id");

        /* (1)
         Get the geometry from the GenericRecord. The value has the following structure, so I have to
         go through "payload", "after", "geometry":

            {
              "meta": {
                "operation": "READ",
                "timestamp": 1694772549729,
                "changedFields": null
              },
              "payload": {
                "dataType": "NODE_GEOMETRY",
                "id": 66,
                "before": null,
                "after": {
                  "com.cross_ni.cross.cdc.model.sink.NodeGeometry": {
                    "geometry": {
                      "bytes": "\u0001\u0001\u0000\u0000\u0000ÄÝÐº\u001fédÁB­¸)JA"
                    }
                  }
                }
              }
            }
         */
        GenericRecord recValue = record.value();
        GenericRecord payload = (GenericRecord) recValue.get("payload");
        GenericRecord after = (GenericRecord) payload.get("after");
        ByteBuffer geometry = (ByteBuffer) after.get("geometry"); // geometry in WKB format - just array of bytes

        /* (2)
        Convert ByteBuffer to hex string - each byte is written in hexadecimal number from 00 to FF
        You can use this string as an input string for PostgreSQL functions

        -- convert hex string to WKT
        st_asText('0101000000C4DDD0BA1FE964C19A42AD80B8294A41')

        -- decode to bytea
        decode('0101000000C4DDD0BA1FE964C19A42AD80B8294A41', 'hex')

        -- convert to geometry
        ST_GeomFromWKB(decode('0101000000C4DDD0BA1FE964C19A42AD80B8294A41', 'hex'), 3857)
         */
        byte[] geometryBytesArr = geometry.array();
        String geometryHexStr = Hex.encodeHexString(geometryBytesArr).toUpperCase();

        // 3
        // Print info about the consumed record
        String msg = String.format(
            "\nNODE_GEOMETRY record consumed.\n  id: %s\n  geom (WKB in hex format): %s",
            nodeGeometryId,
            geometryHexStr);
        System.out.println(msg);

        // 4
        // Check if the limit is exceeded
        if (!(++counter < limit)) {
          return;
        }
      }
    }
  }

  public static Consumer<GenericRecord, GenericRecord> createConsumerAndSubscribe(String topic) {
    final Properties props = new Properties();
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, "example04client");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "example04group");
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroDeserializer");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroDeserializer");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    props.put("schema.registry.url", "http://172.17.0.1:8081");

    final Consumer<GenericRecord, GenericRecord> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Collections.singletonList(topic));
    return consumer;
  }

}
