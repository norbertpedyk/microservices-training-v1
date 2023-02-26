package pl.pedyk.resource.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ResourceKafkaTopicConfig {

    @Value("${spring.kafka.resource-topic}")
    private String kafkaTopic;

    @Bean
    public NewTopic resourceAddedTopic() {
        return TopicBuilder
                .name(kafkaTopic)
                .build();
    }
}
