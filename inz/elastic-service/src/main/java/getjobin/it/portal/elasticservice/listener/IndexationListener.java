package getjobin.it.portal.elasticservice.listener;

import api.IndexationEventDTO;
import getjobin.it.portal.elasticservice.client.document.DocumentService;
import getjobin.it.portal.elasticservice.client.index.control.IndexManagementService;
import getjobin.it.portal.elasticservice.infrastructure.config.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest.OpType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IndexationListener {

    private DocumentService documentService;

    private IndexManagementService indexManagementService;

    @Autowired
    public IndexationListener(DocumentService documentService, IndexManagementService indexManagementService) {
        this.documentService = documentService;
        this.indexManagementService = indexManagementService;
    }

    @StreamListener(KafkaTopic.INDEXATION_TOPIC)
    public void handleIndexationEvent(@Payload IndexationEventDTO indexationEvent) {
        log.info("Received indexation event {}", indexationEvent);

        OpType operationType = OpType.fromString(indexationEvent.getOperationType());

        switch(operationType) {
            case CREATE:
                documentService.createDocument(indexationEvent);
                break;
            case UPDATE:
                documentService.updateDocument(indexationEvent);
                break;
        }
    }
}
