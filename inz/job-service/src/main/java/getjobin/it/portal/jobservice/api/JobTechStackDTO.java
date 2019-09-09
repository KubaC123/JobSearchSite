package getjobin.it.portal.jobservice.api;

import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class JobTechStackDTO {

    private TechStackDTO techStack;
    private Integer experienceLevel;
}
