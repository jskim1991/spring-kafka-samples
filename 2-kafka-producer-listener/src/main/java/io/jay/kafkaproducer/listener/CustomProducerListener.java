package io.jay.kafkaproducer.listener;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.lang.Nullable;

public class CustomProducerListener implements ProducerListener<String, String> {

    @Override
    public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
        System.out.println("Producer Listener - Sent to : " + recordMetadata.topic() + "-" + recordMetadata.partition() + "-" + recordMetadata.offset());
    }

    @Override
    public void onError(ProducerRecord<String, String> producerRecord, @Nullable RecordMetadata recordMetadata, Exception exception) {
        System.out.println("Producer Listener - failed to send record due to " + exception.getMessage());
    }
}
