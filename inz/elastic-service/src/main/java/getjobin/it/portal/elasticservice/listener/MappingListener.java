package getjobin.it.portal.elasticservice.listener;

import getjobin.it.portal.elasticservice.api.MappingEventDto;
import getjobin.it.portal.elasticservice.client.control.ESJavaClient;
import getjobin.it.portal.elasticservice.infrastructure.config.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MappingListener {

    private ESJavaClient esJavaClient;

    @Autowired
    public MappingListener(ESJavaClient esJavaClient) {
        this.esJavaClient = esJavaClient;
    }

    @StreamListener(KafkaTopic.MAPPING_TOPIC)
    public void handleMapping(@Payload MappingEventDto indexMapping) {
        log.info("[MAPPING] Received mapping for index {} from mappings topic {}", indexMapping.getIndexName(), indexMapping);
        if(esJavaClient.indexExist(indexMapping.getIndexName())) {
            esJavaClient.putMapping(indexMapping);
        } else {
            log.info("[MAPPING] Index {} does not exist. Trying to create it", indexMapping.getIndexName());
            esJavaClient.createIndex(indexMapping);
        }
    }
}
