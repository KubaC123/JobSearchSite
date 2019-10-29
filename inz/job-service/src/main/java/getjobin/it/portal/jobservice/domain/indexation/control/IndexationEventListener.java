package getjobin.it.portal.jobservice.domain.indexation.control;

import getjobin.it.portal.elasticservice.api.DocumentEventDto;
import getjobin.it.portal.jobservice.infrastructure.config.kafka.KafkaEventPublisher;
import getjobin.it.portal.jobservice.infrastructure.config.kafka.KafkaTopic;
import getjobin.it.portal.jobservice.infrastructure.exception.JobServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IndexationEventListener {

    @Autowired
    private KafkaTopic kafkaTopic;

    @Autowired
    private KafkaEventPublisher kafkaEventPublisher;

    @EventListener
    public void handleIndexationEvent(DocumentEventDto event) {
        try {
            kafkaEventPublisher.sendEventOnTopic(kafkaTopic.indexation(), event);
            logSuccess(event);
        } catch (Exception exception) {
            logException(event, exception);
            throw new JobServiceException("[INDEXATION] Exception during sending indexation event, check server log for details");
        }
    }

    private void logSuccess(DocumentEventDto event) {
        log.info("[INDEXATION] Event with id {}, operation type {} has been send on kafka topic",
                event.getObjectId(), event.getOperationType().toUpperCase());
    }

    private void logException(DocumentEventDto event, Exception exception) {
        log.info("[INDEXATION] Exception during sending event with id {} and operation type {} on kafka topic \n {}",
                event.getObjectId(), event.getOperationType().toUpperCase(), exception);
    }
}
