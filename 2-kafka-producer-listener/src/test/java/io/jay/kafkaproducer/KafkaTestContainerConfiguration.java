package io.jay.kafkaproducer;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class KafkaTestContainerConfiguration {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Bean
    public ProducerFactory<?, ?> producerFactory() {
        Map<String, Object> configProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), new StringSerializer());
        return producerFactory;
    }
}
