package api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "IndexMappingDTOBuilder")
@Data
@JsonDeserialize(builder = MappingEvent.IndexMappingDTOBuilder.class)
public class MappingEvent {

    private String indexName;
    private String mapping;

    @JsonPOJOBuilder(withPrefix = "")
    public static class IndexMappingDTOBuilder { }
}