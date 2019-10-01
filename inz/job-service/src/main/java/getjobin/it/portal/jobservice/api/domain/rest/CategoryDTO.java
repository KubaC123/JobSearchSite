package getjobin.it.portal.jobservice.api.domain.rest;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CategoryDTO {

    private Long id;
    private String name;
    private Integer jobCounter;
}
