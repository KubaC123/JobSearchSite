package getjobin.it.portal.jobservice.infrastructure.config;

import getjobin.it.portal.jobservice.KafkaTopics;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(KafkaTopics.class)
public class KafkaStreamConfig {
}
