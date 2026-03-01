package co.istad.wallet.notify;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.StreamableKafkaMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @Autowired
    public void configure(EventProcessingConfigurer configurer,
            StreamableKafkaMessageSource<String, byte[]> kafkaMessageSource) {
        configurer.registerTrackingEventProcessor("notification-processing", c -> kafkaMessageSource);
    }
}
