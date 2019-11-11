package getjobin.it.portal.elasticservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder(builderClassName = "FoundDocumentDtoBuilder")
@Data
@JsonDeserialize(builder = DocumentDto.FoundDocumentDtoBuilder.class)
public class DocumentDto {

    Float score;
    Long objectId;
    Map<String, Object> data;

    @JsonPOJOBuilder(withPrefix = "")
    public static class FoundDocumentDtoBuilder { }
}
