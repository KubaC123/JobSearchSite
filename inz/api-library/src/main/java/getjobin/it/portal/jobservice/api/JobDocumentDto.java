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
    private Long companyId;
    private String companyName;
    private Long categoryId;
    private String categoryName;
    private String experienceLevel;
    private String employmentType;
    private Integer salaryMin;
    private Integer salaryMax;
    private Boolean flexibleWorkHours;
    private String description;
    private Long technologyId;
    private String technologyName;
    private String projectDescription;
    private Integer development;
    private Integer testing;
    private Integer maintenance;
    private Integer clientSupport;
    private Integer meetings;
    private Integer leading;
    private Integer documentation;
    private Integer otherActivities;
    private Boolean active;
    private List<JobTechStackDocumentDto> techStacks;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JobDocumentDtoBuilder { }
}
