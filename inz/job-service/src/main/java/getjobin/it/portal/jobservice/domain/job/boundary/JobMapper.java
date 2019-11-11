package getjobin.it.portal.jobservice.domain.job.boundary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import getjobin.it.portal.elasticservice.api.DocumentDto;
import getjobin.it.portal.elasticservice.api.DocumentEventDto;
import getjobin.it.portal.jobservice.api.JobDocumentDto;
import getjobin.it.portal.jobservice.api.JobDto;
import getjobin.it.portal.jobservice.api.JobSimpleDto;
import getjobin.it.portal.jobservice.domain.category.boundary.CategoryMapper;
import getjobin.it.portal.jobservice.domain.company.boundary.CompanyMapper;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.JobWithRelatedObjects;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.domain.technology.boundary.TechnologyMapper;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@ApplicationScope
@Slf4j
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

    public List<JobSimpleDto> toSimpleDtos(List<DocumentDto> jobs) {
        return jobs.stream()
                .map(this::toSimleDto)
                .collect(Collectors.toList());
    }

    private JobSimpleDto toSimleDto(DocumentDto job) {
        return JobSimpleDto.builder()
                .id(job.getObjectId())
                .score(job.getScore())
                .title((String)job.getData().get("title"))
                .companyName((String)job.getData().get("companyName"))
                .salaryMin(Integer.valueOf((String)job.getData().get("salaryMin")))
                .salaryMax(Integer.valueOf((String)job.getData().get("salaryMax")))
                .cities(parseCities(job.getData()))
                .techStacks(parseTechStacks(job.getData()))
                .build();
    }

    private List<String> parseCities(Map<String, Object> documentData) {
        String commaSeparatedCities = (String) documentData.get("cities");
        return Arrays.asList(commaSeparatedCities.split(","));
    }

    private List<String> parseTechStacks(Map<String, Object> documentData) {
        String commaSeparatedTechStacks = (String) documentData.get("techStacks");
        return Arrays.asList(commaSeparatedTechStacks.split(","));
    }

    Job updateExistingJobOffer(Job existingJob, JobDto jobDto) {
        Job.JobEntityBuilder builder = Job.toBuilder(existingJob);
        addReferenceAttributes(jobDto, builder);
        addCommonAttributes(jobDto, builder);
        return builder.build();
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    public JobDocumentDto toJobDocumentDto(JobWithRelatedObjects jobWithRelatedObjects) {
        JobDocumentDto.JobDocumentDtoBuilder builder = JobDocumentDto.builder();
        addReferenceAttributes(jobWithRelatedObjects, builder);
        return builder
                .id(jobWithRelatedObjects.getJob().getId())
                .type(jobWithRelatedObjects.getJob().getType())
                .title(jobWithRelatedObjects.getJob().getTitle())
                .experienceLevel(jobWithRelatedObjects.getJob().getExperienceLevel())
                .employmentType(jobWithRelatedObjects.getJob().getEmploymentType())
                .salaryMin(String.valueOf(jobWithRelatedObjects.getJob().getSalaryMin()))
                .salaryMax(String.valueOf(jobWithRelatedObjects.getJob().getSalaryMax()))
                .description(jobWithRelatedObjects.getJob().getDescription())
                .projectDescription(jobWithRelatedObjects.getJob().getProjectDescription())
                .active(jobWithRelatedObjects.getJob().getActive())
                .build();
    }

    public void addReferenceAttributes(JobWithRelatedObjects jobWithRelatedObjects, JobDocumentDto.JobDocumentDtoBuilder builder) {
        addTechStacks(builder, jobWithRelatedObjects);
        addLocations(builder, jobWithRelatedObjects);
        addOtherReferenceAttributes(jobWithRelatedObjects.getJob(), builder);
    }

    private void addLocations(JobDocumentDto.JobDocumentDtoBuilder builder, JobWithRelatedObjects jobWithRelatedObjects) {
        List<Location> locations = jobWithRelatedObjects.getLocationsWithRemote().stream()
                .map(Pair::getFirst)
                .collect(Collectors.toList());
        builder.cities(locations.stream()
                .map(Location::getCity)
                .collect(Collectors.joining(", ")));
        builder.countries(locations.stream()
                .map(Location::getCountryName)
                .collect(Collectors.joining(", ")));
        builder.streets(locations.stream()
                .map(Location::getStreet)
                .collect(Collectors.joining(", ")));
    }

    private void addTechStacks(JobDocumentDto.JobDocumentDtoBuilder builder, JobWithRelatedObjects jobWithRelatedObjects) {
        builder.techStacks(jobWithRelatedObjects.getTechStacksWithExperienceLevel().stream()
                .map(Pair::getFirst)
                .map(TechStack::getName)
                .collect(Collectors.joining(", ")));
    }

    private void addOtherReferenceAttributes(Job job, JobDocumentDto.JobDocumentDtoBuilder builder) {
        Optional.ofNullable(job.getCompany()).ifPresent(company -> {
            builder.companyName(company.getName());
            builder.companyDescription(company.getDescription());
        });
        Optional.ofNullable(job.getCategory()).ifPresent(category -> builder.categoryName(category.getName()));
        Optional.ofNullable(job.getTechnology()).ifPresent(technology -> builder.technologyName(technology.getName()));
    }

    public DocumentEventDto toDocumentEventDto(JobWithRelatedObjects job) {
        JobDocumentDto jobDocumentDto = toJobDocumentDto(job);
        try {
            return DocumentEventDto.builder()
                    .objectId(jobDocumentDto.getId())
                    .index("job")
                    .data(objectMapper.writeValueAsString(jobDocumentDto))
                    .build();
        } catch (JsonProcessingException exception) {
            log.warn("Exception during parsing JobDocumentDto {} to JSON. {}", jobDocumentDto,  exception);
            return null;
        }
    }
}
