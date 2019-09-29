package getjobin.it.portal.jobservice;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface KafkaTopics {

    String MAPPING_TOPIC = "mappings";

    @Output(MAPPING_TOPIC)
    MessageChannel mappingTopic();
}
