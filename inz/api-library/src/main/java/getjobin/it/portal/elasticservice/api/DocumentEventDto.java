package getjobin.it.portal.elasticservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "DocumentEventDtoBuilder")
@Data
@JsonDeserialize(builder = DocumentEventDto.DocumentEventDtoBuilder.class)
public class DocumentEventDto {

    private Long objectId;
    private String index;
    private String operationType;
    private String data;

    @JsonPOJOBuilder(withPrefix = "")
    public static class DocumentEventDtoBuilder { }
}
