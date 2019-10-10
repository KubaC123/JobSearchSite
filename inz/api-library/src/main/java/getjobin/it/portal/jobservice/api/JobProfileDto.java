package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "JobProfileDtoBuilder")
@Data
@JsonDeserialize(builder = JobProfileDto.JobProfileDtoBuilder.class)
public class JobProfileDto {

    private Integer development;
    private Integer testing;
    private Integer maintenance;
    private Integer clientSupport;
    private Integer meetings;
    private Integer leading;
    private Integer documentation;
    private Integer otherActivities;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JobProfileDtoBuilder { }
}
