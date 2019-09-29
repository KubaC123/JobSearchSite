package getjobin.it.portal.jobservice.api.client;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class IndexMappingDTO {

    private String indexName;
    private String mapping;
}
