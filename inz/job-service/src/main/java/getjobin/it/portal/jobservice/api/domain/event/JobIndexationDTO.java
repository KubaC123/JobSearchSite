package getjobin.it.portal.jobservice.api.domain.event;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class JobIndexationDTO {

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
    private List<JobTechStackIndexationDTO> techStacks;
}
