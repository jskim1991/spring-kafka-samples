package io.jay.kafkaproducer.callback;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@RequiredArgsConstructor
public class SampleProducerWithCallback {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ListenableFutureCallback<SendResult<String, String>> customProducerCallback;

    public void send(Message<?> message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(message);
        future.addCallback(customProducerCallback);
    }
}
