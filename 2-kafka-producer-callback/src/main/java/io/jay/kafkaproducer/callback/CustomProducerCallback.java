package io.jay.kafkaproducer.callback;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class CustomProducerCallback implements ListenableFutureCallback<SendResult<String, String>> {

    @Override
    public void onFailure(Throwable ex) {
        System.out.println("Callback - send error: " + ex.getMessage());
    }

    @Override
    public void onSuccess(SendResult<String, String> result) {
        RecordMetadata recordMetadata = result.getRecordMetadata();
        System.out.println("Callback - sent to: " + recordMetadata.topic() + "-" + recordMetadata.partition() + "-" + recordMetadata.offset());
    }
}
