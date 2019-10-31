package getjobin.it.portal.jobservice.domain.indexation.boundary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import getjobin.it.portal.elasticservice.api.DocumentEventDto;
import getjobin.it.portal.jobservice.api.JobDocumentDto;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.JobWithRelatedObjects;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class IndexationMapper {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JobService jobService;

    public JobDocumentDto toJobDocumentDto(Job job) {
        JobDocumentDto.JobDocumentDtoBuilder builder = JobDocumentDto.builder();
        addReferenceAttributes(job, builder);

        return builder
                .id(job.getId())
                .type(job.getType())
                .title(job.getTitle())
                .experienceLevel(job.getExperienceLevel())
                .employmentType(job.getEmploymentType())
                .salaryMin(String.valueOf(job.getSalaryMin()))
                .salaryMax(String.valueOf(job.getSalaryMax()))
                .description(job.getDescription())
                .projectDescription(job.getProjectDescription())
                .active(job.getActive())
                .build();
    }

    private void addReferenceAttributes(Job job, JobDocumentDto.JobDocumentDtoBuilder builder) {
        JobWithRelatedObjects jobWithRelatedObjects = jobService.getJobWithRelatedObjects(job);
        addTechStacks(builder, jobWithRelatedObjects);
        addLocations(builder, jobWithRelatedObjects);
        addOtherReferenceAttributes(job, builder);
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

    public List<DocumentEventDto> toDocumentEventDtos(List<Job> jobs) {
        return jobs.stream()
                .map(this::toDocumentEventDto)
                .collect(Collectors.toList());
    }

    public DocumentEventDto toDocumentEventDto(Job job) {
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
