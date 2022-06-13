package io.jay.kafkaproducer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentCaptor.*;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka
@ContextConfiguration(classes = {KafkaTestContainerConfiguration.class})
class CustomerProducerListenerTests {

    @SpyBean
    ProducerListener mockCustomProducerListener;

    @Autowired
    KafkaTemplate<?, ?> kafkaTemplate;

    @Test
    void test_onSuccess_isInvoked() throws InterruptedException, ExecutionException, TimeoutException {
        Message<String> message = MessageBuilder.withPayload("message payload")
                .setHeader(KafkaHeaders.TOPIC, "topic-1")
                .build();


        kafkaTemplate.send(message).get();


        ArgumentCaptor<ProducerRecord> producerRecordArgument = forClass(ProducerRecord.class);
        ArgumentCaptor<RecordMetadata> recordMetadataArgument = forClass(RecordMetadata.class);
        verify(mockCustomProducerListener, timeout(1000L)).onSuccess(producerRecordArgument.capture(), recordMetadataArgument.capture());

        ProducerRecord producerRecord = producerRecordArgument.getValue();
        assertThat(producerRecord.topic(), equalTo("topic-1"));
        RecordMetadata recordMetadata = recordMetadataArgument.getValue();
        assertThat(recordMetadata.offset(), equalTo(0L));
    }
}