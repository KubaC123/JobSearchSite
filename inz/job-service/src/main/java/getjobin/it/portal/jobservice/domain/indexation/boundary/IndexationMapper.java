package getjobin.it.portal.jobservice.domain.indexation.boundary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import getjobin.it.portal.elasticservice.api.DocumentEventDto;
import getjobin.it.portal.jobservice.api.JobDocumentDto;
import getjobin.it.portal.jobservice.domain.job.control.OperationType;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.JobTechStackRelation;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class IndexationMapper {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TechStackService techStackService;

    public JobDocumentDto toJobDocumentDto(Job job) {
        JobDocumentDto.JobDocumentDtoBuilder builder = JobDocumentDto.builder();
        Optional.ofNullable(job.getCompany()).ifPresent(company -> {
            builder.companyName(company.getName());
            builder.companyDescription(company.getDescription());
        });
        Optional.ofNullable(job.getCategory()).ifPresent(category -> builder.categoryName(category.getName()));
        Optional.ofNullable(job.getTechnology()).ifPresent(technology -> builder.technologyName(technology.getName()));
        job.getTechStackRelations().ifPresent(techStackRelations -> {
            builder.techStacks(techStackRelations.stream()
                    .map(JobTechStackRelation::getTechStackId)
                    .map(techStackService::getById)
                    .map(TechStack::getName)
                    .collect(Collectors.joining(", ")));
        });
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

    public DocumentEventDto toIndexationEvent(Job job, OperationType operationType) {
        JobDocumentDto jobDocumentDto = toJobDocumentDto(job);
        try {
            return DocumentEventDto.builder()
                    .objectId(jobDocumentDto.getId())
                    .index("job")
                    .operationType(operationType.getLiteral())
                    .data(objectMapper.writeValueAsString(jobDocumentDto))
                    .build();
        } catch (JsonProcessingException exception) {
            log.warn("Exception during parsing JobDocumentDto {} to JSON. {}", jobDocumentDto,  exception);
            return null;
        }
    }
}
