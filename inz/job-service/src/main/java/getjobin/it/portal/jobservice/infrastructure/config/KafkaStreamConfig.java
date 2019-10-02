package getjobin.it.portal.jobservice.infrastructure.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(KafkaTopic.class)
public class KafkaStreamConfig {
}
