package api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "IndexMappingDTOBuilder")
@Data
@JsonDeserialize(builder = IndexMappingDTO.IndexMappingDTOBuilder.class)
public class IndexMappingDTO {

    private String indexName;
    private String mapping;

    @JsonPOJOBuilder(withPrefix = "")
    public static class IndexMappingDTOBuilder { }
}
