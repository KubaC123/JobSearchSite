package getjobin.it.portal.jobservice.api.domain.rest;

import lombok.Builder;
import lombok.Value;

import java.net.URI;

@Builder
@Value
public class ResourceDTO {

    private String objectType;
    private Long objectId;
    private URI resourceURI;
}
