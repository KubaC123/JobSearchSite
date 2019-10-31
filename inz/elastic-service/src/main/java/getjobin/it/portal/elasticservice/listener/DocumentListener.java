package getjobin.it.portal.elasticservice.listener;

import getjobin.it.portal.elasticservice.api.DocumentEventDto;
import getjobin.it.portal.elasticservice.client.control.ESJavaClient;
import getjobin.it.portal.elasticservice.infrastructure.config.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DocumentListener {

    private ESJavaClient esJavaClient;

    @Autowired
    public DocumentListener(ESJavaClient esJavaClient) {
        this.esJavaClient = esJavaClient;
    }

    @StreamListener(KafkaTopic.INDEXATION_TOPIC)
    public void handleIndexationEvent(@Payload DocumentEventDto documentEvent) {
        log.info("[INDEXATION] Received indexation event {}", documentEvent);

        if(esJavaClient.documentExists(documentEvent)) {
            esJavaClient.updateDocument(documentEvent);
        } else {
            esJavaClient.indexDocument(documentEvent);
        }
    }
}
