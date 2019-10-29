package getjobin.it.portal.jobservice.domain.job.boundary;

import getjobin.it.portal.jobservice.api.JobDto;
import getjobin.it.portal.jobservice.domain.category.boundary.CategoryMapper;
import getjobin.it.portal.jobservice.domain.company.boundary.CompanyMapper;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.JobWithRelatedObjects;
import getjobin.it.portal.jobservice.domain.technology.boundary.TechnologyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class JobMapper {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TechnologyMapper technologyMapper;

    @Autowired
    private JobTechStackRelationMapper techStackRelationMapper;

    @Autowired
    private JobLocationRelationMapper locationRelationMapper;

    public Job toEntity(JobDto jobDto) {
        Job.JobEntityBuilder builder = Job.builder();
        builder.id(jobDto.getId());
        addReferenceAttributes(jobDto, builder);
        addCommonAttributes(jobDto, builder);
        return builder.build();
    }

    private void addReferenceAttributes(JobDto jobDto, Job.JobEntityBuilder builder) {
        Optional.ofNullable(jobDto.getCompany()).ifPresent(company -> {
            builder.company(companyMapper.toEntity(company));
        });
        Optional.ofNullable(jobDto.getCategory()).ifPresent(category-> {
            builder.category(categoryMapper.toEntity(category));
        });
        Optional.ofNullable(jobDto.getTechnology()).ifPresent(technology -> {
            builder.technology(technologyMapper.toEntity(technology));
        });
    }

    private void addCommonAttributes(JobDto jobDto, Job.JobEntityBuilder builder) {
        builder.type(jobDto.getType())
                .title(jobDto.getTitle())
                .experienceLevel(jobDto.getExperienceLevel())
                .employmentType(jobDto.getEmploymentType())
                .salaryMin(jobDto.getSalaryMin())
                .salaryMax(jobDto.getSalaryMax())
                .startDate(jobDto.getStartDate())
                .contractDuration(jobDto.getContractDuration())
                .flexibleWorkHours(jobDto.getFlexibleWorkHours())
                .currency(jobDto.getCurrency())
                .description(jobDto.getDescription())
                .projectIndustry(jobDto.getProjectIndustry())
                .projectTeamSize(jobDto.getProjectTeamSize())
                .projectDescription(jobDto.getProjectDescription())
                .development(jobDto.getDevelopment())
                .testing(jobDto.getTesting())
                .maintenance(jobDto.getMaintenance())
                .clientSupport(jobDto.getClientSupport())
                .meetings(jobDto.getMeetings())
                .leading(jobDto.getLeading())
                .documentation(jobDto.getDocumentation())
                .otherActivities(jobDto.getOtherActivities())
                .agreements(jobDto.getAgreements())
                .techStackRelations(techStackRelationMapper.toEntities(jobDto.getId(), jobDto.getTechStacks()))
                .locationRelations(locationRelationMapper.toEntities(jobDto.getId(), jobDto.getLocations()));
    }

    List<JobDto> toDtos(List<JobWithRelatedObjects> jobs) {
        return jobs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    JobDto toDto(JobWithRelatedObjects jobWithRelatedObjects) {
        JobDto.JobDtoBuilder builder = JobDto.builder();
        Job job = jobWithRelatedObjects.getJob();
        return builder
                .id(job.getId())
                .type(job.getType())
                .title(job.getTitle())
                .company(companyMapper.toDto(job.getCompany()))
                .category(categoryMapper.toDto(job.getCategory()))
                .experienceLevel(job.getExperienceLevel())
                .employmentType(job.getEmploymentType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .startDate(job.getStartDate())
                .contractDuration(job.getContractDuration())
                .flexibleWorkHours(job.getFlexibleWorkHours())
                .currency(job.getCurrency())
                .description(job.getDescription())
                .technology(technologyMapper.toDto(job.getTechnology()))
                .projectIndustry(job.getProjectIndustry())
                .projectTeamSize(job.getProjectTeamSize())
                .projectDescription(job.getProjectDescription())
                .development(job.getDevelopment())
                .testing(job.getTesting())
                .maintenance(job.getMaintenance())
                .clientSupport(job.getClientSupport())
                .meetings(job.getMeetings())
                .leading(job.getLeading())
                .documentation(job.getDocumentation())
                .otherActivities(job.getOtherActivities())
                .agreements(job.getAgreements())
                .techStacks(techStackRelationMapper.toDtos(jobWithRelatedObjects.getTechStacksWithExperienceLevel()))
                .locations(locationRelationMapper.toDtos(jobWithRelatedObjects.getLocationsWithRemote()))
                .applications(job.getApplications())
                .build();
    }

    Job updateExistingJobOffer(Job existingJob, JobDto jobDto) {
        Job.JobEntityBuilder builder = Job.toBuilder(existingJob);
        addReferenceAttributes(jobDto, builder);
        addCommonAttributes(jobDto, builder);
        return builder.build();
    }
}
