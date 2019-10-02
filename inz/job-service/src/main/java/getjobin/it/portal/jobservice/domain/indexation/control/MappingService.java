package getjobin.it.portal.jobservice.domain.indexation.control;

import getjobin.it.portal.jobservice.api.client.IndexMappingDTO;
import getjobin.it.portal.jobservice.infrastructure.config.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
@Slf4j
public class MappingService {

    private KafkaTopic kafkaTopic;

    @Autowired
    public MappingService(KafkaTopic kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public void sendMappingOnTopic(IndexMappingDTO indexMapping) {
        try {
            log.info("Sending mapping on kafka topic {}", indexMapping);
            kafkaTopic.mappings()
                    .send(MessageBuilder
                            .withPayload(indexMapping)
                            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                            .build());
        } catch (Exception exception) {
            log.warn("Exception during sending mapping on kafka topic {}. {}", indexMapping, exception);
        }
    }
}
