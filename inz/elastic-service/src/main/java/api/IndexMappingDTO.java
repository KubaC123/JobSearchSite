package api;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class IndexMappingDTO {

    @NonNull
    private String indexName;
    @NonNull
    private String mapping;
}
