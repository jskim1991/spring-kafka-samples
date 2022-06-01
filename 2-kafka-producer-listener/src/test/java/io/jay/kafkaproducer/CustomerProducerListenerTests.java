package io.jay.kafkaproducer;

import io.jay.kafkaproducer.listener.CustomProducerListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka
@ContextConfiguration(classes = {KafkaTestContainerConfiguration.class})
class CustomerProducerListenerTests {

    @Autowired
    ProducerFactory<String, String> producerFactory;

    @Mock
    CustomProducerListener mockCustomProducerListener;

    KafkaTemplate<String, String> kafkaTemplate;

    private String testTopic = "topic-1";

    @BeforeEach
    void setup() {
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setProducerListener(mockCustomProducerListener);
    }

    @Test
    void test_onSuccess_isInvoked() throws InterruptedException, ExecutionException {
        Message<String> message = MessageBuilder.withPayload("message payload")
                .setHeader(KafkaHeaders.TOPIC, testTopic)
                .build();


        kafkaTemplate.send(message).get();


        verify(mockCustomProducerListener).onSuccess(any(), any());
    }
}