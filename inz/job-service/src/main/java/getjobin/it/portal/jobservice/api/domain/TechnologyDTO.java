package getjobin.it.portal.jobservice.api.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class TechnologyDTO {

    private Long id;
    private String name;
    private String imageUrl;
}