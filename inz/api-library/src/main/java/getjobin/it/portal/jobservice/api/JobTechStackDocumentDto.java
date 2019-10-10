package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "JobTechStackDocumentDtoBuilder")
@Data
@JsonDeserialize(builder = JobTechStackDocumentDto.JobTechStackDocumentDtoBuilder.class)
public class JobTechStackDocumentDto {

    private Long techStackId;
    private String techStackName;
    private Integer techStackExperienceLevel;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JobTechStackDocumentDtoBuilder { }
}
