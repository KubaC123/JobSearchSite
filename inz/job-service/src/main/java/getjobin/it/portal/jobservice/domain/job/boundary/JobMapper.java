package getjobin.it.portal.jobservice.domain.job.boundary;

import getjobin.it.portal.jobservice.api.domain.event.JobEvent;
import getjobin.it.portal.jobservice.api.domain.rest.JobDTO;
import getjobin.it.portal.jobservice.api.domain.rest.JobProfileDTO;
import getjobin.it.portal.jobservice.api.domain.rest.JobTechStackDTO;
import getjobin.it.portal.jobservice.api.domain.rest.ResourceDTO;
import getjobin.it.portal.jobservice.domain.category.boundary.CategoryResource;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import getjobin.it.portal.jobservice.domain.company.boundary.CompanyResource;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.technology.boundary.TechnologyResource;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.techstack.boundary.TechStackMapper;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.infrastructure.util.IdsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JobMapper {

    private TechStackMapper techStackMapper;
    private JobTechStackRelationMapper techStackRelationMapper;
    private TechStackService techStackService;

    @Autowired
    public JobMapper(TechStackMapper techStackMapper, JobTechStackRelationMapper techStackRelationMapper, TechStackService techStackService) {
        this.techStackMapper = techStackMapper;
        this.techStackRelationMapper = techStackRelationMapper;
        this.techStackService = techStackService;
    }

    public Job toEntity(JobDTO jobDTO) {
        Job.JobOfferEntityBuilder builder = Job.builder();
        addCompany(jobDTO, builder);
        addCategory(jobDTO, builder);
        addTechnology(jobDTO, builder);
        addJobProfile(jobDTO, builder);
        return builder
                .id(jobDTO.getId())
                .type(jobDTO.getType())
                .title(jobDTO.getTitle())
                .experienceLevel(jobDTO.getExperienceLevel())
                .employmentType(jobDTO.getEmploymentType())
                .salaryMin(jobDTO.getSalaryMin())
                .salaryMax(jobDTO.getSalaryMax())
                .startDate(jobDTO.getStartDate())
                .contractDuration(jobDTO.getContractDuration())
                .flexibleWorkHours(jobDTO.getFlexibleWorkHours())
                .currency(jobDTO.getCurrency())
                .description(jobDTO.getDescription())
                .projectIndustry(jobDTO.getProjectIndustry())
                .projectTeamSize(jobDTO.getProjectTeamSize())
                .projectDescription(jobDTO.getProjectDescription())
                .agreements(jobDTO.getAgreements())
                .techStackRelations(techStackRelationMapper.toEntities(jobDTO.getId(), jobDTO.getTechStacks()))
                .build();
    }

    private void addCompany(JobDTO jobDTO, Job.JobOfferEntityBuilder builder) {
        Optional.ofNullable(jobDTO.getCompany()).ifPresent(company -> builder.company(Company.builder()
                .withId(company.getObjectId())
                .build()));
    }

    private void addCategory(JobDTO jobDTO, Job.JobOfferEntityBuilder builder) {
        Optional.ofNullable(jobDTO.getCategory()).ifPresent(category -> builder.category(Category.builder()
                .withId(category.getObjectId())
                .build()));
    }

    private void addTechnology(JobDTO jobDTO, Job.JobOfferEntityBuilder builder) {
        Optional.ofNullable(jobDTO.getTechnology()).ifPresent(technology -> builder.technology(Technology.builder()
                .withId(technology.getObjectId())
                .build()));
    }

    private void addJobProfile(JobDTO jobDTO, Job.JobOfferEntityBuilder builder) {
        Optional.ofNullable(jobDTO.getJobProfile()).ifPresent(jobProfile -> builder
                .development(jobProfile.getDevelopment())
                .testing(jobProfile.getTesting())
                .maintenance(jobProfile.getMaintenance())
                .clientSupport(jobProfile.getClientSupport())
                .meetings(jobProfile.getMeetings())
                .leading(jobProfile.getLeading())
                .documentation(jobProfile.getDocumentation())
                .otherActivities(jobProfile.getOtherActivities()));
    }

    public JobEvent toEvent(Long jobId, OperationType operationType) {
        return JobEvent.builder()
                .jobId(jobId)
                .operationType(operationType)
                .build();
    }

    public JobDTO toDTO(Job job) {
        JobDTO.JobDTOBuilder builder = JobDTO.builder();
        Optional.ofNullable(job.getCompany()).ifPresent(company -> builder.company(ResourceDTO.builder()
                .objectId(company.getId())
                .objectType(Company.COMPANY_TYPE)
                .resourceURI(getCompanyResourceURI(company.getId()))
                .build()));
        Optional.ofNullable(job.getCategory()).ifPresent(category -> builder.category(ResourceDTO.builder()
                .objectId(category.getId())
                .objectType(Category.CATEGORY_TYPE)
                .resourceURI(getCategoryResourceURI(category.getId()))
                .build()));
        Optional.ofNullable(job.getTechnology()).ifPresent(technology -> builder.technology(ResourceDTO.builder()
                .objectId(technology.getId())
                .objectType(Technology.TECHNOLOGY_TYPE)
                .resourceURI(getTechnologyResourceURI(technology.getId()))
                .build()));
        job.getTechStackRelations().ifPresent(techStackRelations -> builder.techStacks(techStackRelations.stream()
                .map(techStackRelation -> JobTechStackDTO.builder()
                        .techStack(techStackMapper.toDTO(techStackService.getById(techStackRelation.getTechStackId())))
                        .experienceLevel(techStackRelation.getExperienceLevel())
                        .build())
                .collect(Collectors.toList())));
        return builder
                .id(job.getId())
                .type(job.getType())
                .title(job.getTitle())
                .experienceLevel(job.getExperienceLevel())
                .employmentType(job.getEmploymentType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .startDate(job.getStartDate())
                .contractDuration(job.getContractDuration())
                .flexibleWorkHours(job.getFlexibleWorkHours())
                .currency(job.getCurrency())
                .description(job.getDescription())
                .projectIndustry(job.getProjectIndustry())
                .projectTeamSize(job.getProjectTeamSize())
                .projectDescription(job.getProjectDescription())
                .jobProfile(JobProfileDTO.builder()
                        .development(job.getDevelopment())
                        .testing(job.getTesting())
                        .maintenance(job.getMaintenance())
                        .clientSupport(job.getClientSupport())
                        .meetings(job.getMeetings())
                        .leading(job.getLeading())
                        .documentation(job.getDocumentation())
                        .otherActivities(job.getOtherActivities())
                        .build())
                .agreements(job.getAgreements())
                .applications(job.getApplications())
                .build();
    }

    private URI getCompanyResourceURI(Long companyId) {
        return ControllerLinkBuilder.linkTo(ControllerLinkBuilder
                .methodOn(CompanyResource.class)
                .browseCompanies(new IdsParam(String.valueOf(companyId))))
                .toUri();
    }

    private URI getCategoryResourceURI(Long categoryId) {
        return ControllerLinkBuilder.linkTo(ControllerLinkBuilder
                .methodOn(CategoryResource.class)
                .browseCategories(new IdsParam(String.valueOf(categoryId))))
                .toUri();
    }

    private URI getTechnologyResourceURI(Long technologyId) {
        return ControllerLinkBuilder.linkTo(ControllerLinkBuilder
                .methodOn(TechnologyResource.class)
                .browseTechnologies(new IdsParam(String.valueOf(technologyId))))
                .toUri();
    }

    public Job updateExistingJobOffer(Job existingJob, JobDTO jobDTO) {
        Job.JobOfferEntityBuilder builder = Job.toBuilder(existingJob)
                .title(jobDTO.getTitle())
                .experienceLevel(jobDTO.getExperienceLevel())
                .employmentType(jobDTO.getEmploymentType())
                .salaryMin(jobDTO.getSalaryMin())
                .salaryMax(jobDTO.getSalaryMax())
                .startDate(jobDTO.getStartDate())
                .contractDuration(jobDTO.getContractDuration())
                .flexibleWorkHours(jobDTO.getFlexibleWorkHours())
                .currency(jobDTO.getCurrency())
                .description(jobDTO.getDescription())
                .projectIndustry(jobDTO.getProjectIndustry())
                .projectTeamSize(jobDTO.getProjectTeamSize())
                .projectDescription(jobDTO.getProjectDescription())
                .agreements(jobDTO.getAgreements())
                .techStackRelations(techStackRelationMapper.toEntities(existingJob.getId(), jobDTO.getTechStacks()));
        addCategory(jobDTO, builder);
        addTechnology(jobDTO, builder);
        addJobProfile(jobDTO, builder);
        return builder.build();
    }
}
