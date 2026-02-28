package co.istad.wallet.query;

import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.StreamableKafkaMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalletQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletQueryApplication.class, args);
    }

    @Autowired
    public void configure(EventProcessingConfigurer configurer,
                          StreamableKafkaMessageSource<String, byte[]> kafkaMessageSource){
        configurer.registerTrackingEventProcessor("wallet-projection", c -> kafkaMessageSource);
        configurer.registerTrackingEventProcessor("transaction-projection", c -> kafkaMessageSource);
    }
}
