package io.jay.kafkaproducer;

import io.jay.kafkaproducer.callback.SampleProducerWithCallback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SampleProducerWithCallbackTests {

    @Mock
    private ListenableFutureCallback<SendResult<String, String>> mockProducerCallback;

    @Mock
    private KafkaTemplate<String, String> mockKafkaTemplate;
    private SampleProducerWithCallback producer;

    private ListenableFuture mockFuture;
    private RecordMetadata recordMetadata;
    private SendResult<String, String> mockSendResult;

    private Message<String> message;

    @BeforeEach
    void setUp() {
        producer = new SampleProducerWithCallback(mockKafkaTemplate, mockProducerCallback);

        mockFuture = mock(ListenableFuture.class);
        recordMetadata = new RecordMetadata(new TopicPartition("topic", 0), 1L, 0, 0L, 0, 0);

        mockSendResult = mock(SendResult.class);
        when(mockSendResult.getRecordMetadata())
                .thenReturn(recordMetadata);

        message = MessageBuilder.withPayload("test")
                .setHeader(KafkaHeaders.TOPIC, "topic")
                .build();
    }

    @Test
    void test_onSuccess_isCalled() {
        when(mockKafkaTemplate.send(message))
                .thenReturn(mockFuture);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback callback = invocationOnMock.getArgument(0);
            callback.onSuccess(mockSendResult);
            return null;
        }).when(mockFuture).addCallback(any(ListenableFutureCallback.class));


        producer.send(message);


        verify(mockProducerCallback).onSuccess(mockSendResult);
    }

    @Test
    void test_onFailure_isCalled() {
        RuntimeException ex = new RuntimeException("error");
        when(mockKafkaTemplate.send(message))
                .thenReturn(mockFuture);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback callback = invocationOnMock.getArgument(0);
            callback.onFailure(ex);
            return null;
        }).when(mockFuture).addCallback(any(ListenableFutureCallback.class));


        producer.send(message);


        verify(mockProducerCallback).onFailure(ex);
    }
}