package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder(builderClassName = "JobDtoBuilder")
@Data
@JsonDeserialize(builder = JobDto.JobDtoBuilder.class)
public class JobDto {

    private Long id;
    private String type;
    private String title;
    private ResourceDto company;
    private ResourceDto category;
    private String experienceLevel;
    private String employmentType;
    private Integer salaryMin;
    private Integer salaryMax;
    private String startDate;
    private String contractDuration;
    private Boolean flexibleWorkHours;
    private String currency;
    private String description;
    private ResourceDto technology;
    private String projectIndustry;
    private Integer projectTeamSize;
    private String projectDescription;
    private JobProfileDto jobProfile;
    private String agreements;
    private Boolean remote;
    private List<JobTechStackDto> techStacks;
    private Integer applications;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JobDtoBuilder { }
}
