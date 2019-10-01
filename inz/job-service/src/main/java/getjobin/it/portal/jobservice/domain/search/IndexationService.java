package getjobin.it.portal.jobservice.domain.search;

import getjobin.it.portal.jobservice.infrastructure.config.KafkaTopics;
import getjobin.it.portal.jobservice.api.client.IndexMappingDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
@Slf4j
public class IndexationService {

    private final KafkaTopics kafkaTopics;

    @Autowired
    public IndexationService(KafkaTopics kafkaTopics) {
        this.kafkaTopics = kafkaTopics;
    }

    public void sendMappingOnTopic(IndexMappingDTO indexMapping) {
        log.info("Sending mapping on mappings topic {}", indexMapping);
        kafkaTopics.mappingTopic()
                .send(MessageBuilder
                        .withPayload(indexMapping)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .build());
    }
}
