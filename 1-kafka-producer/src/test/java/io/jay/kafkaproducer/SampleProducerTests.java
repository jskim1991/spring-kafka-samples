package io.jay.kafkaproducer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@EmbeddedKafka
@ContextConfiguration(classes = {KafkaTestContainerConfiguration.class})
class SampleProducerTests {

    private String testTopic = "topic-1";

    @Autowired
    private SampleProducer producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private KafkaMessageListenerContainer<String, String> container;
    private BlockingQueue<ConsumerRecord<String, String>> consumerRecords;

    @BeforeEach
    void setup() {
        consumerRecords = new LinkedBlockingQueue<>();

        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("test-group-id", "false", embeddedKafkaBroker);
        DefaultKafkaConsumerFactory<String, String> consumer = new DefaultKafkaConsumerFactory<>(consumerProperties);

        ContainerProperties containerProperties = new ContainerProperties(testTopic);
        container = new KafkaMessageListenerContainer<>(consumer, containerProperties);
        container.setupMessageListener((MessageListener<String, String>) record -> consumerRecords.add(record));
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @AfterEach
    void after() {
        container.stop();
    }

    @Test
    void test_send_sendsMessageToBroker() throws InterruptedException {
        producer.send(MessageBuilder.withPayload("message payload")
                .setHeader(KafkaHeaders.TOPIC, testTopic)
                .build());


        ConsumerRecord<String, String> consumedRecord = consumerRecords.poll(1, TimeUnit.SECONDS);
        assertThat(consumedRecord.topic(), equalTo(testTopic));
        assertThat(consumedRecord.key(), is(nullValue()));
        assertThat(consumedRecord.value(), equalTo("message payload"));
    }
}
