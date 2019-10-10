package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "JobTechStackDtoBuilder")
@Data
@JsonDeserialize(builder = JobTechStackDto.JobTechStackDtoBuilder.class)
public class JobTechStackDto {

    private TechStackDto techStack;
    private Integer experienceLevel;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JobTechStackDtoBuilder { }
}
