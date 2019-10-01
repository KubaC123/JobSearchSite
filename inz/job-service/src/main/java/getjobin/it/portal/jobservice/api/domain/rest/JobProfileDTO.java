package getjobin.it.portal.jobservice.api.domain.rest;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class JobProfileDTO {

    private Integer development;
    private Integer testing;
    private Integer maintenance;
    private Integer clientSupport;
    private Integer meetings;
    private Integer leading;
    private Integer documentation;
    private Integer otherActivities;
}
