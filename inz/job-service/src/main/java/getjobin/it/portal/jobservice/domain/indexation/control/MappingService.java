package getjobin.it.portal.jobservice.domain.indexation.control;

import getjobin.it.portal.elasticservice.api.MappingEventDto;
import getjobin.it.portal.jobservice.infrastructure.config.KafkaEventPublisher;
import getjobin.it.portal.jobservice.infrastructure.config.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MappingService {

    private KafkaTopic kafkaTopic;

    private KafkaEventPublisher kafkaEventPublisher;

    @Autowired
    public MappingService(KafkaTopic kafkaTopic, KafkaEventPublisher kafkaEventPublisher) {
        this.kafkaTopic = kafkaTopic;
        this.kafkaEventPublisher = kafkaEventPublisher;
    }

    public void sendMappingOnTopic(MappingEventDto indexMapping) {
        try {
            kafkaEventPublisher.sendEventOnTopic(kafkaTopic.mappings(), indexMapping);
            log.info("Mapping for index {} has been send on kafka topic", indexMapping.getIndexName());
        } catch (Exception exception) {
            log.warn("Exception during sending mapping on kafka topic {}. {}", indexMapping, exception);
        }
    }
}
