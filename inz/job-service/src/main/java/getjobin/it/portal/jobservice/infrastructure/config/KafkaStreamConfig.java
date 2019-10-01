package getjobin.it.portal.jobservice.infrastructure.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(KafkaTopics.class)
public class KafkaStreamConfig {
}
