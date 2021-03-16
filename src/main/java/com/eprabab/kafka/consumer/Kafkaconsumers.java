package com.eprabab.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface Kafkaconsumers {
    void send(ConsumerRecord<?,?> consumerRecord);
}
