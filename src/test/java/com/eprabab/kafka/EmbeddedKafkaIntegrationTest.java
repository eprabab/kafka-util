package com.eprabab.kafka;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import com.eprabab.kafka.consumer.KafkaConsumer;
import com.eprabab.kafka.producer.KafkaProducer;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class EmbeddedKafkaIntegrationTest {

    @Autowired
    private KafkaProducer producer;

    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private TestCallBack testCallBack;

    @Value("${kafka.topic}")
    private String topic;

    @BeforeAll
    public void setUp() {
        consumer.register(testCallBack);
    }

    @Test
    public void verifyKafkaMessageSentAndReceived() throws InterruptedException {

        while(testCallBack.getReceivedData().size() == 0) {
            producer.sendMessage(topic, "1234-id");
            System.out.println("waiting for data from kafka");
            Thread.sleep(1000);
        }
        final List<ConsumerRecord<?, ?>> dataFromKafka = testCallBack.getReceivedData();
        final String data = (String) dataFromKafka.get(0).value();
        assertEquals("1234-id",data);
    }
}

