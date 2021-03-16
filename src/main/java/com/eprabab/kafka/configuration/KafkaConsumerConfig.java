package com.eprabab.kafka.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import lombok.extern.slf4j.Slf4j;
import static com.eprabab.kafka.utils.Utils.loadPropertiesAsMap;

@EnableKafka
@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Value("${kafka.consumer.config}")
    private String consumerConfig;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() throws IOException {

        Map<String, Object> configProps;

        if (StringUtils.isBlank(consumerConfig)) {
            log.warn("Kafka consumer properties not set, loading default properties");
            configProps = getDefaultProperties();
        } else {
            configProps = loadPropertiesAsMap(consumerConfig);
        }

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    private Map<String, Object> getDefaultProperties() {
        final Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() throws IOException {
        final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }
}
