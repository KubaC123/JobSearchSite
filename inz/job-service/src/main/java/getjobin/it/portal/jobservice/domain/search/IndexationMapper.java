package getjobin.it.portal.jobservice.domain.search;

import getjobin.it.portal.jobservice.api.domain.event.JobIndexationDTO;
import getjobin.it.portal.jobservice.api.domain.event.JobTechStackIndexationDTO;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.JobTechStackRelation;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class IndexationMapper {

    private TechStackService techStackService;

    @Autowired
    public IndexationMapper(TechStackService techStackService) {
        this.techStackService = techStackService;
    }

    public JobIndexationDTO toJobEventDTO(Job job) {
        JobIndexationDTO.JobIndexationDTOBuilder builder = JobIndexationDTO.builder();
        Optional.ofNullable(job.getCompany()).ifPresent(company -> {
            builder.companyId(company.getId());
            builder.companyName(company.getName());
        });
        Optional.ofNullable(job.getCategory()).ifPresent(category -> {
            builder.categoryId(category.getId());
            builder.categoryName(category.getName());
        });
        Optional.ofNullable(job.getTechnology()).ifPresent(technology -> {
            builder.technologyId(technology.getId());
            builder.technologyName(technology.getName());
        });
        job.getTechStackRelations().ifPresent(techStackRelations -> {
            builder.techStacks(techStackRelations.stream()
                    .map(this::toJobTechStackEventDTO)
                    .collect(Collectors.toList()));
        });
        return builder
                .type(job.getType())
                .title(job.getTitle())
                .experienceLevel(job.getExperienceLevel())
                .employmentType(job.getEmploymentType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .flexibleWorkHours(job.getFlexibleWorkHours())
                .description(job.getDescription())
                .projectDescription(job.getProjectDescription())
                .development(job.getDevelopment())
                .testing(job.getTesting())
                .maintenance(job.getMaintenance())
                .clientSupport(job.getClientSupport())
                .meetings(job.getMeetings())
                .leading(job.getLeading())
                .documentation(job.getDocumentation())
                .otherActivities(job.getOtherActivities())
                .active(job.getActive())
                .build();
    }

    private JobTechStackIndexationDTO toJobTechStackEventDTO(JobTechStackRelation techStackRelation) {
        return JobTechStackIndexationDTO.builder()
                .techStackId(techStackRelation.getTechStackId())
                .techStackName(techStackService.getById(techStackRelation.getTechStackId()).getName())
                .techStackExperienceLevel(techStackRelation.getExperienceLevel())
                .build();
    }
}
