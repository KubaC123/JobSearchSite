package getjobin.it.portal.jobservice.infrastructure.config.kafka;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(KafkaTopic.class)
public class KafkaStreamConfig {
}
