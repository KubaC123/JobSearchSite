package getjobin.it.portal.jobservice.api;

import lombok.Builder;
import lombok.Getter;

import java.net.URI;

@Builder @Getter
public class ResourceDTO {

    private String objectType;
    private Long objectId;
    private URI resourceURI;
}
