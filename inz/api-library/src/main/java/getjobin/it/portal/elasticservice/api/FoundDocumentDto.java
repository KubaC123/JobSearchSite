package getjobin.it.portal.elasticservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder(builderClassName = "FoundDocumentDtoBuilder")
@Data
@JsonDeserialize(builder = FoundDocumentDto.FoundDocumentDtoBuilder.class)
public class FoundDocumentDto {

    Float score;
    Long objectId;
    Map<String, Object> data;

    @JsonPOJOBuilder
    public static class FoundDocumentDtoBuilder { }
}
