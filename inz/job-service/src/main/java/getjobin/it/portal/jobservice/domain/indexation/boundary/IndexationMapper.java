package getjobin.it.portal.jobservice.domain.indexation.boundary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import getjobin.it.portal.elasticservice.api.DocumentEventDto;
import getjobin.it.portal.jobservice.api.JobDocumentDto;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.JobWithRelatedObjects;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class IndexationMapper {

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
