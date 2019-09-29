package getjobin.it.portal.elasticservice.config;

import getjobin.it.portal.elasticservice.infrastructure.KafkaTopics;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(KafkaTopics.class)
public class KafkaStreamConfig {
}
