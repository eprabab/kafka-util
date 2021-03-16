package com.eprabab.kafka.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import lombok.extern.slf4j.Slf4j;
import static com.eprabab.kafka.utils.Utils.loadPropertiesAsMap;

@Slf4j
@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.producer.config}")
    private String producerConfigPath;

    @Bean
    public ProducerFactory<String, Object> producerFactory() throws IOException {
        Map<String, Object> configProps;

        if (StringUtils.isBlank(producerConfigPath)) {
            log.warn("Kafka producer properties not set, loading default properties");
            configProps = getDefaultProperties();
        } else {
            configProps = loadPropertiesAsMap(producerConfigPath);
        }

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private Map<String, Object> getDefaultProperties() {
        final Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return configProps;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() throws IOException {
        return new KafkaTemplate<>(producerFactory());
    }
}
