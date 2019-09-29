package getjobin.it.portal.jobservice.api.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class TechStackDTO {

    private Long id;
    private String name;
}
