package com.eprabab.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.eprabab.kafka.metrics.MetricsRegistry;
import com.google.common.util.concurrent.AtomicDouble;

@Service
public class KafkaProducer<V> {

    @Autowired
    private KafkaTemplate<String, V> kafkaTemplate;

    @Autowired
    private MetricsRegistry metricsRegistry;

    public void sendMessage(final String topic, final V data) {
        final ListenableFuture<SendResult<String, V>> future = kafkaTemplate.send(topic, data); // send data to broker
        future.addCallback(new ListenableFutureCallback<SendResult<String, V>>() {

            @Override
            public void onSuccess(final SendResult<String, V> stringStringSendResult) {
                final AtomicDouble failureGauge = metricsRegistry.createOrGetMetrics("producer.send", "success");
                failureGauge.getAndAdd(1);
            }

            @Override
            public void onFailure(final Throwable throwable) {
                final AtomicDouble failureGauge = metricsRegistry.createOrGetMetrics("producer.send", "failure");
                failureGauge.getAndAdd(1);
            }
        });
    }
}
