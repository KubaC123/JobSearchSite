package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder(builderClassName = "JobDocumentDtoBuilder")
@Data
@JsonDeserialize(builder = JobDocumentDto.JobDocumentDtoBuilder.class)
public class JobDocumentDto {

    private Long id;
    private String type;
    private String title;
    private String companyName;
    private String companyDescription;
    private String categoryName;
    private String experienceLevel;
    private String employmentType;
    private String salaryMin;
    private String salaryMax;
    private String description;
    private String technologyName;
    private String projectDescription;
    private Boolean active;
    private String techStacks;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JobDocumentDtoBuilder { }
}
