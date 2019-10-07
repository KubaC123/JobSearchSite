package api;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Builder
@Value
public class DocumentDTO {

    Float score;
    Long objectId;
    Map<String, Object> data;
}
