package getjobin.it.portal.jobservice.api;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder @Getter
public class JobDTO {

    private Long id;
    private String type;
    private String title;
    private ResourceDTO company;
    private String experienceLevel;
    private String employmentType;
    private Integer salaryMin;
    private Integer salaryMax;
    private String currency;
    private String description;
    private ResourceDTO technology;
    private String agreements;
    private Boolean remote;
    private List<JobTechStackDTO> techStacks;
    private Integer applications;
}
