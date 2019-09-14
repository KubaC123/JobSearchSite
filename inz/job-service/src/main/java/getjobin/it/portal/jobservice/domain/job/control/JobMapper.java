package getjobin.it.portal.jobservice.domain.job.control;

import getjobin.it.portal.jobservice.api.JobDTO;
import getjobin.it.portal.jobservice.api.JobTechStackDTO;
import getjobin.it.portal.jobservice.api.ResourceDTO;
import getjobin.it.portal.jobservice.domain.company.boundary.CompanyResource;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.technology.boundary.TechnologyResource;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackMapper;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.infrastructure.IdsParam;
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
        Optional.ofNullable(jobDTO.getCompany()).ifPresent(company -> builder.company(Company.builder()
                .withId(company.getObjectId())
                .build()));
        Optional.ofNullable(jobDTO.getTechnology()).ifPresent(technology -> builder.technology(Technology.builder()
                .withId(technology.getObjectId())
                .build()));
        return builder
                .id(jobDTO.getId())
                .type(jobDTO.getType())
                .title(jobDTO.getTitle())
                .experienceLevel(jobDTO.getExperienceLevel())
                .employmentType(jobDTO.getEmploymentType())
                .salaryMin(jobDTO.getSalaryMin())
                .salaryMax(jobDTO.getSalaryMax())
                .currency(jobDTO.getCurrency())
                .description(jobDTO.getDescription())
                .agreements(jobDTO.getAgreements())
                .remote(jobDTO.getRemote())
                .techStackRelations(techStackRelationMapper.toEntities(jobDTO.getId(), jobDTO.getTechStacks()))
                .build();
    }

    public JobDTO toDTO(Job job) {
        JobDTO.JobDTOBuilder builder = JobDTO.builder();
        Optional.ofNullable(job.getCompany()).ifPresent(company -> builder.company(ResourceDTO.builder()
                .objectId(company.getId())
                .objectType(Company.COMPANY_TYPE)
                .resourceURI(getCompanyResourceURI(company.getId()))
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
                .currency(job.getCurrency())
                .description(job.getDescription())
                .agreements(job.getAgreements())
                .remote(job.getRemote())
                .applications(job.getApplications())
                .build();
    }

    private URI getCompanyResourceURI(Long companyId) {
        return ControllerLinkBuilder.linkTo(ControllerLinkBuilder
                .methodOn(CompanyResource.class)
                .browseCompanies(new IdsParam(String.valueOf(companyId))))
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
                .currency(jobDTO.getCurrency())
                .description(jobDTO.getDescription())
                .agreements(jobDTO.getAgreements())
                .remote(jobDTO.getRemote())
                .techStackRelations(techStackRelationMapper.toEntities(existingJob.getId(), jobDTO.getTechStacks()));
        Optional.ofNullable(jobDTO.getTechnology()).ifPresent(technology -> builder.technology(Technology.builder()
                .withId(technology.getObjectId())
                .build()));
        return builder.build();
    }
}
