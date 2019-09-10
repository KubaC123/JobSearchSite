package getjobin.it.portal.jobservice.domain.joboffer.control;

import getjobin.it.portal.jobservice.api.JobOfferDTO;
import getjobin.it.portal.jobservice.api.JobTechStackDTO;
import getjobin.it.portal.jobservice.api.ResourceDTO;
import getjobin.it.portal.jobservice.domain.company.boundary.CompanyResource;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.joboffer.entity.JobOffer;
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
public class JobOfferMapper {

    private TechStackMapper techStackMapper;
    private JobTechStackRelationMapper techStackRelationMapper;
    private TechStackService techStackService;

    @Autowired
    public JobOfferMapper(TechStackMapper techStackMapper, JobTechStackRelationMapper techStackRelationMapper, TechStackService techStackService) {
        this.techStackMapper = techStackMapper;
        this.techStackRelationMapper = techStackRelationMapper;
        this.techStackService = techStackService;
    }

    public JobOffer toEntity(JobOfferDTO jobOfferDTO) {
        JobOffer.JobOfferEntityBuilder builder = JobOffer.builder();
        Optional.ofNullable(jobOfferDTO.getCompany()).ifPresent(company -> builder.withCompany(Company.builder()
                .withId(company.getObjectId())
                .build()));
        Optional.ofNullable(jobOfferDTO.getTechnology()).ifPresent(technology -> builder.withTechnology(Technology.builder()
                .withId(technology.getObjectId())
                .build()));
        return builder
                .withId(jobOfferDTO.getId())
                .withType(jobOfferDTO.getType())
                .withTitle(jobOfferDTO.getTitle())
                .withExperienceLevel(jobOfferDTO.getExperienceLevel())
                .withEmploymentType(jobOfferDTO.getEmploymentType())
                .withSalaryMin(jobOfferDTO.getSalaryMin())
                .withSalaryMax(jobOfferDTO.getSalaryMax())
                .withCurrency(jobOfferDTO.getCurrency())
                .withDescription(jobOfferDTO.getDescription())
                .withAgreements(jobOfferDTO.getAgreements())
                .withRemote(jobOfferDTO.getRemote())
                .withTechStackRelations(techStackRelationMapper.toEntities(jobOfferDTO.getId(), jobOfferDTO.getTechStacks()))
                .build();
    }

    public JobOfferDTO toDTO(JobOffer jobOffer) {
        JobOfferDTO.JobOfferDTOBuilder builder = JobOfferDTO.builder();
        Optional.ofNullable(jobOffer.getCompany()).ifPresent(company -> builder.company(ResourceDTO.builder()
                .objectId(company.getId())
                .objectType(Company.COMPANY_TYPE)
                .resourceURI(getCompanyResourceURI(company.getId()))
                .build()));
        Optional.ofNullable(jobOffer.getTechnology()).ifPresent(technology -> builder.technology(ResourceDTO.builder()
                .objectId(technology.getId())
                .objectType(Technology.TECHNOLOGY_TYPE)
                .resourceURI(getTechnologyResourceURI(technology.getId()))
                .build()));
        return builder
                .id(jobOffer.getId())
                .type(jobOffer.getType())
                .title(jobOffer.getTitle())
                .experienceLevel(jobOffer.getExperienceLevel())
                .employmentType(jobOffer.getEmploymentType())
                .salaryMin(jobOffer.getSalaryMin())
                .salaryMax(jobOffer.getSalaryMax())
                .currency(jobOffer.getCurrency())
                .description(jobOffer.getDescription())
                .agreements(jobOffer.getAgreements())
                .remote(jobOffer.getRemote())
                .applications(jobOffer.getApplications())
                .techStacks(jobOffer.getTechStackRelations().stream()
                        .map(techStackRelation -> JobTechStackDTO.builder()
                                .techStack(techStackMapper.toDTO(techStackService.getById(techStackRelation.getTechStackId())))
                                .experienceLevel(techStackRelation.getExperienceLevel())
                                .build())
                        .collect(Collectors.toList()))
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

    public JobOffer updateExistingJobOffer(JobOffer existingJobOffer, JobOfferDTO jobOfferDTO) {
        JobOffer.JobOfferEntityBuilder builder = JobOffer.toBuilder(existingJobOffer)
                .withTitle(jobOfferDTO.getTitle())
                .withExperienceLevel(jobOfferDTO.getExperienceLevel())
                .withEmploymentType(jobOfferDTO.getEmploymentType())
                .withSalaryMin(jobOfferDTO.getSalaryMin())
                .withSalaryMax(jobOfferDTO.getSalaryMax())
                .withCurrency(jobOfferDTO.getCurrency())
                .withDescription(jobOfferDTO.getDescription())
                .withAgreements(jobOfferDTO.getAgreements())
                .withRemote(jobOfferDTO.getRemote())
                .withTechStackRelations(techStackRelationMapper.toEntities(existingJobOffer.getId(), jobOfferDTO.getTechStacks()));
        Optional.ofNullable(jobOfferDTO.getTechnology()).ifPresent(technology -> builder.withTechnology(Technology.builder()
                .withId(technology.getObjectId())
                .build()));
        return builder.build();
    }
}
