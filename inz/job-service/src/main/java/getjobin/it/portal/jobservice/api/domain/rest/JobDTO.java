package getjobin.it.portal.jobservice.api.domain.rest;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class JobDTO {

    private Long id;
    private String type;
    private String title;
    private ResourceDTO company;
    private ResourceDTO category;
    private String experienceLevel;
    private String employmentType;
    private Integer salaryMin;
    private Integer salaryMax;
    private String startDate;
    private String contractDuration;
    private Boolean flexibleWorkHours;
    private String currency;
    private String description;
    private ResourceDTO technology;
    private String projectIndustry;
    private Integer projectTeamSize;
    private String projectDescription;
    private JobProfileDTO jobProfile;
    private String agreements;
    private Boolean remote;
    private List<JobTechStackDTO> techStacks;
    private Integer applications;
}
