package getjobin.it.portal.elasticservice.infrastructure;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface KafkaTopics {

    String MAPPING_TOPIC = "mappings";

    @Input(MAPPING_TOPIC)
    SubscribableChannel mappingTopic();
}
