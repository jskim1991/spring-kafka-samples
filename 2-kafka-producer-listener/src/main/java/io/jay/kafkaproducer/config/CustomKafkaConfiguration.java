package io.jay.kafkaproducer.config;

import io.jay.kafkaproducer.listener.CustomProducerListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.ProducerListener;

@Configuration
@RequiredArgsConstructor
public class CustomKafkaConfiguration {

    @Bean
    public ProducerListener producerListener() {
        return new CustomProducerListener();
    }
}
