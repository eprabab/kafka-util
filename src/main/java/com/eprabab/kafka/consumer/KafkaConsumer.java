package com.eprabab.kafka.consumer;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    final List<Callback> clients = new ArrayList<>();

    @KafkaListener(topics = "${kafka.topic}")
    public void syncRead(final ConsumerRecord<?, ?> consumerRecord, final Acknowledgment acknowledgment) {
        for(final Callback client: clients) {
            client.call(consumerRecord);
        }

        acknowledgment.acknowledge();
    }

    public void register(final Callback callback) {
        clients.add(callback);
    }
}

