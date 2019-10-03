package getjobin.it.portal.elasticservice.listener;

import api.IndexMappingDTO;
import getjobin.it.portal.elasticservice.client.index.control.IndexManagementService;
import getjobin.it.portal.elasticservice.infrastructure.config.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@Slf4j
public class MappingListener {

    private IndexManagementService indexManagementService;

    @Autowired
    public MappingListener(IndexManagementService indexManagementService) {
        this.indexManagementService = indexManagementService;
    }

    @StreamListener(KafkaTopic.MAPPING_TOPIC)
    public void handleMapping(@Payload IndexMappingDTO indexMapping) {
        log.info(MessageFormat.format("Received mapping for index {0} from mappings topic.", indexMapping.getIndexName()));
        if(indexManagementService.indexExist(indexMapping.getIndexName())) {
            indexManagementService.putMapping(indexMapping);
            log.info(MessageFormat.format("Mapping for index {0} has been updated.", indexMapping.getIndexName()));
        } else {
            log.info(MessageFormat.format("Index {0} does not exist. Trying to create it.", indexMapping.getIndexName()));
            indexManagementService.createIndex(indexMapping);
        }
    }
}
