package getjobin.it.portal.jobservice.domain.job.boundary;

import getjobin.it.portal.jobservice.api.JobDto;
import getjobin.it.portal.jobservice.api.JobLocationDto;
import getjobin.it.portal.jobservice.api.JobTechStackDto;
import getjobin.it.portal.jobservice.api.ResourceDto;
import getjobin.it.portal.jobservice.domain.category.boundary.CategoryResource;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import getjobin.it.portal.jobservice.domain.company.boundary.CompanyResource;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.location.boundary.LocationMapper;
import getjobin.it.portal.jobservice.domain.location.control.LocationService;
import getjobin.it.portal.jobservice.domain.technology.boundary.TechnologyResource;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.techstack.boundary.TechStackMapper;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.infrastructure.rest.IdsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JobMapper {

    @Autowired
    private TechStackMapper techStackMapper;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private JobTechStackRelationMapper techStackRelationMapper;

    @Autowired
    private JobLocationRelationMapper locationRelationMapper;

    @Autowired
    private TechStackService techStackService;

    @Autowired
    private LocationService locationService;

    public Job toEntity(JobDto jobDto) {
        Job.JobEntityBuilder builder = Job.builder();
        addCompany(jobDto, builder);
        addCategory(jobDto, builder);
        addTechnology(jobDto, builder);
        builder.id(jobDto.getId());
        addCommonAttributes(jobDto, builder);
        return builder.build();
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

    private void addCompany(JobDto jobDto, Job.JobEntityBuilder builder) {
        Optional.ofNullable(jobDto.getCompany()).ifPresent(company -> builder.company(Company.builder()
                .withId(company.getObjectId())
                .build()));
    }

    private void addCategory(JobDto jobDto, Job.JobEntityBuilder builder) {
        Optional.ofNullable(jobDto.getCategory()).ifPresent(category -> builder.category(Category.builder()
                .withId(category.getObjectId())
                .build()));
    }

    private void addTechnology(JobDto jobDto, Job.JobEntityBuilder builder) {
        Optional.ofNullable(jobDto.getTechnology()).ifPresent(technology -> builder.technology(Technology.builder()
                .withId(technology.getObjectId())
                .build()));
    }

    List<JobDto> toDtos(List<Job> jobs) {
        return jobs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    JobDto toDto(Job job) {
        JobDto.JobDtoBuilder builder = JobDto.builder();
        Optional.ofNullable(job.getCompany()).ifPresent(company -> builder.company(ResourceDto.builder()
                .objectId(company.getId())
                .objectType(Company.COMPANY_TYPE)
                .resourceURI(getCompanyResourceURI(company.getId()))
                .build()));
        Optional.ofNullable(job.getCategory()).ifPresent(category -> builder.category(ResourceDto.builder()
                .objectId(category.getId())
                .objectType(Category.CATEGORY_TYPE)
                .resourceURI(getCategoryResourceURI(category.getId()))
                .build()));
        Optional.ofNullable(job.getTechnology()).ifPresent(technology -> builder.technology(ResourceDto.builder()
                .objectId(technology.getId())
                .objectType(Technology.TECHNOLOGY_TYPE)
                .resourceURI(getTechnologyResourceURI(technology.getId()))
                .build()));
        job.getTechStackRelations().ifPresent(techStackRelations -> builder.techStacks(techStackRelations.stream()
                .map(techStackRelation -> JobTechStackDto.builder()
                        .techStack(techStackMapper.toDto(techStackService.getById(techStackRelation.getTechStackId())))
                        .experienceLevel(techStackRelation.getExperienceLevel())
                        .build())
                .collect(Collectors.toList())));
        job.getLocationRelations().ifPresent(locationRelations -> builder.locations(locationRelations.stream()
                .map(locationRelation -> JobLocationDto.builder()
                        .location(locationMapper.toDto(locationService.getById(locationRelation.getLocationId())))
                        .remote(locationRelation.getRemote())
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
                .development(job.getDevelopment())
                .testing(job.getTesting())
                .maintenance(job.getMaintenance())
                .clientSupport(job.getClientSupport())
                .meetings(job.getMeetings())
                .leading(job.getLeading())
                .documentation(job.getDocumentation())
                .otherActivities(job.getOtherActivities())
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

    Job updateExistingJobOffer(Job existingJob, JobDto jobDto) {
        Job.JobEntityBuilder builder = Job.toBuilder(existingJob);
        addCommonAttributes(jobDto, builder);
        addCategory(jobDto, builder);
        addTechnology(jobDto, builder);
        return builder.build();
    }
}
