package co.istad.wallet.notify.config;

import org.apache.kafka.common.TopicPartition;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.jackson3.Jackson3Serializer;
import org.axonframework.serialization.jackson3.Jackson3SerializerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.KeyDeserializer;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.module.SimpleModule;

@Configuration
public class AxonConfig {

    private Jackson3SerializerCustomizer customizer() {
        return builder -> {
            SimpleModule module = new SimpleModule();
            module.addKeyDeserializer(TopicPartition.class, new KeyDeserializer() {
                @Override
                public Object deserializeKey(String key, DeserializationContext ctx) {
                    int lastDash = key.lastIndexOf('-');
                    String topic = key.substring(0, lastDash);
                    int partition = Integer.parseInt(key.substring(lastDash + 1));
                    return new TopicPartition(topic, partition);
                }
            });

            builder
                    .addModule(module)
                    .polymorphicTypeValidator(
                            BasicPolymorphicTypeValidator.builder()
                                    .allowIfBaseType(Object.class)
                                    .build()
                    )
                    .activateDefaultTyping(
                            BasicPolymorphicTypeValidator.builder()
                                    .allowIfBaseType(Object.class)
                                    .build(),
                            DefaultTyping.JAVA_LANG_OBJECT
                    );
        };
    }

    @Bean
    @Primary
    public Serializer serializer() {
        return Jackson3Serializer.builder()
                .jsonMapperBuilderCustomizer(customizer())
                .build();
    }

    @Bean
    public Serializer eventSerializer() {
        return Jackson3Serializer.builder()
                .jsonMapperBuilderCustomizer(customizer())
                .build();
    }

    @Bean
    public Serializer messageSerializer() {
        return Jackson3Serializer.builder()
                .jsonMapperBuilderCustomizer(customizer())
                .build();
    }
}
