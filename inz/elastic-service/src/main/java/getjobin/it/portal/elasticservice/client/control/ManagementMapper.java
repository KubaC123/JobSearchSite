package getjobin.it.portal.elasticservice.client.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import getjobin.it.portal.elasticservice.api.MappingEventDto;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ManagementMapper {

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<MappingEventDto> toIndexMappingDTOs(Map<String, MappingMetaData> mappings) {
        return mappings.entrySet()
                .stream()
                .map(mapping -> MappingEventDto.builder()
                        .indexName(mapping.getKey())
                        .mapping(tryParseMapping(mapping.getValue()))
                        .build())
                .collect(Collectors.toList());
    }

    private String tryParseMapping(MappingMetaData mapping) {
        try {
            return objectMapper.writeValueAsString(mapping.getSourceAsMap());
        } catch (JsonProcessingException exception) {
            log.warn("Exception when parsing mapping " + mapping, exception);
            return null;
        }
    }
}
