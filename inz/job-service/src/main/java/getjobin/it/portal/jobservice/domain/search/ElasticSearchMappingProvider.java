package getjobin.it.portal.jobservice.domain.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ElasticSearchMappingProvider {

    private ObjectMapper objectMapper = new ObjectMapper();

    public String getCompanyMapping() {
        Map<String, String> properties = new HashMap<>();
        properties.put("name", JSONMappingType.TEXT.getValue());
        properties.put("size", JSONMappingType.INTEGER.getValue());
        properties.put("description", JSONMappingType.TEXT.getValue());
        try {
            return objectMapper.writeValueAsString(ImmutableMap.of("properties", properties));
        } catch (JsonProcessingException exception) {
            return null;
        }
    }

    private enum JSONMappingType {
        TEXT("text"),
        KEYWORD("keyword"),
        INTEGER("integer"),
        DATE("date");

        private String dataType;

        JSONMappingType(String dataType) {
            this.dataType = dataType;
        }

        public String getValue() {
            return "{ \"type\": \"" + dataType + "\" } ";
        }
    }
}
