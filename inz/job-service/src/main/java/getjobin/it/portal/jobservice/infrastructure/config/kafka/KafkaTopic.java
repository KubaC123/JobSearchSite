package getjobin.it.portal.jobservice.infrastructure.config.kafka;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface KafkaTopic {

    String MAPPING_TOPIC = "mappings";
    String INDEXATION_TOPIC = "indexation";

    @Output(MAPPING_TOPIC)
    MessageChannel mappings();

    @Output(INDEXATION_TOPIC)
    MessageChannel indexation();
}
