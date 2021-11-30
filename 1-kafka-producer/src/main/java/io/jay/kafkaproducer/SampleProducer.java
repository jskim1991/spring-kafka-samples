package io.jay.kafkaproducer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class SampleProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    public SampleProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Message<?> message) {
        kafkaTemplate.send(message);
    }
}
