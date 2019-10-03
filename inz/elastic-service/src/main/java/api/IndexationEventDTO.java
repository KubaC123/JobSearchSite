package api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "IndexationEventDTOBuilder")
@Data
@JsonDeserialize(builder = IndexationEventDTO.IndexationEventDTOBuilder.class)
public class IndexationEventDTO {

    private Long objectId;
    private String index;
    private String operationType;
    private String data;

    @JsonPOJOBuilder(withPrefix = "")
    public static class IndexationEventDTOBuilder { }
}
