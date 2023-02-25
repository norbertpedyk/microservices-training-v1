package pl.pedyk.resource.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ResourceKafkaTopicConfig {

    @Bean
    public NewTopic postTopic() {
        return TopicBuilder
                .name("postTopic")
                .build();
    }
}
