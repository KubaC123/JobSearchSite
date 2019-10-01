package getjobin.it.portal.jobservice.api.client;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class IndexationEventDTO {

    private Long objectId;
    private String index;
    private String operationType;
    private String data;
}
