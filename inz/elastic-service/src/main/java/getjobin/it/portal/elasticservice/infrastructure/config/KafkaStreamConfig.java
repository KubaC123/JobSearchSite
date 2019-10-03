package getjobin.it.portal.elasticservice.infrastructure.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(KafkaTopic.class)
public class KafkaStreamConfig {
}
