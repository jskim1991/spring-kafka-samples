package io.jay.kafkaproducer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

@RestController
@RequiredArgsConstructor
class ProducerController {

    private final SampleProducer producer;

    @GetMapping("/send")
    public void send() {
        producer.send(MessageBuilder
                .withPayload("test message")
                .setHeader(KafkaHeaders.TOPIC, "test-topic")
                .build());
    }
}
