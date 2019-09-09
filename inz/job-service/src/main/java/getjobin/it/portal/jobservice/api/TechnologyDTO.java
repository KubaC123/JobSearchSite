package getjobin.it.portal.jobservice.api;

import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class TechnologyDTO {

    private Long id;
    private String name;
    private String imageUrl;
}
