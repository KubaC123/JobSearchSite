package getjobin.it.portal.jobservice.domain.indexation.control;

import getjobin.it.portal.elasticservice.api.MappingEventDto;
import getjobin.it.portal.jobservice.infrastructure.config.kafka.KafkaEventPublisher;
import getjobin.it.portal.jobservice.infrastructure.config.kafka.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MappingService {

    @Autowired
    private KafkaTopic kafkaTopic;

    @Autowired
    private KafkaEventPublisher kafkaEventPublisher;

    public void sendMappingOnTopic(MappingEventDto indexMapping) {
        try {
            kafkaEventPublisher.sendEventOnTopic(kafkaTopic.mappings(), indexMapping);
            log.info("Mapping for index {} has been send on kafka topic", indexMapping.getIndexName());
        } catch (Exception exception) {
            log.warn("Exception during sending mapping on kafka topic {}. {}", indexMapping, exception);
        }
    }
}
