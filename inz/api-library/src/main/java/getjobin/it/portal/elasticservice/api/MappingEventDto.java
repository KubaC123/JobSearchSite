package getjobin.it.portal.elasticservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "MappingEventDtoBuilder")
@Data
@JsonDeserialize(builder = MappingEventDto.MappingEventDtoBuilder.class)
public class MappingEventDto {

    private String indexName;
    private String mapping;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MappingEventDtoBuilder { }
}
