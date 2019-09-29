package getjobin.it.portal.elasticservice.event;

import api.IndexMappingDTO;
import getjobin.it.portal.elasticservice.client.management.control.ElasticManagementService;
import getjobin.it.portal.elasticservice.infrastructure.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@Slf4j
public class MappingListener {

    private ElasticManagementService elasticManagementService;

    @Autowired
    public MappingListener(ElasticManagementService elasticManagementService) {
        this.elasticManagementService = elasticManagementService;
    }

    @StreamListener(KafkaTopics.MAPPING_TOPIC)
    public void handleMapping(@Payload IndexMappingDTO indexMapping) {
        log.info(MessageFormat.format("Received mapping for index {0} from mappings topic.", indexMapping.getIndexName()));
        if(elasticManagementService.indexExist(indexMapping.getIndexName())) {
            elasticManagementService.putMapping(indexMapping);
        } else {
            log.info(MessageFormat.format("Ignoring mapping event. Index {0} does not exist.", indexMapping.getIndexName()));
        }
    }
}
