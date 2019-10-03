package getjobin.it.portal.elasticservice.infrastructure.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface KafkaTopic {

    String MAPPING_TOPIC = "mappings";

    String INDEXATION_TOPIC = "indexation";

    @Input(MAPPING_TOPIC)
    SubscribableChannel mappingTopic();

    @Input(INDEXATION_TOPIC)
    SubscribableChannel indexationTopic();
}
