package getjobin.it.portal.elasticservice.listener;

import api.MappingEvent;
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
    public void handleMapping(@Payload MappingEvent indexMapping) {
        log.info("Received mapping for index {} from mappings topic.", indexMapping.getIndexName());
        if(esJavaClient.indexExist(indexMapping.getIndexName())) {
            esJavaClient.putMapping(indexMapping);
            log.info("Mapping for index {} has been updated.", indexMapping.getIndexName());
        } else {
            log.info("Index {} does not exist. Trying to create it.", indexMapping.getIndexName());
            esJavaClient.createIndex(indexMapping);
        }
    }
}
