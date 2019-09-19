package getjobin.it.portal.jobservice.api;

import lombok.Builder;
import lombok.Value;

@Builder @Value
public class JobTechStackDTO {

    private TechStackDTO techStack;
    private Integer experienceLevel;
}
