package api;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class SearchResult {

    Integer count;
    List<DocumentDTO> documents;
}
