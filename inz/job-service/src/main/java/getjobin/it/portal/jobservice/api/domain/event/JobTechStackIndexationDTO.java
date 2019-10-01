package getjobin.it.portal.jobservice.api.domain.event;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class JobTechStackIndexationDTO {

    private Long techStackId;
    private String techStackName;
    private Integer techStackExperienceLevel;
}
