package getjobin.it.portal.jobservice.domain.indexation.boundary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import getjobin.it.portal.elasticservice.api.DocumentEventDto;
import getjobin.it.portal.jobservice.api.JobDocumentDto;
import getjobin.it.portal.jobservice.api.JobTechStackDocumentDto;
import getjobin.it.portal.jobservice.domain.job.boundary.OperationType;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.JobTechStackRelation;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class IndexationMapper {

    private TechStackService techStackService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public IndexationMapper(TechStackService techStackService) {
        this.techStackService = techStackService;
    }

    public JobDocumentDto toJobDocumentDto(Job job) {
        JobDocumentDto.JobDocumentDtoBuilder builder = JobDocumentDto.builder();
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
                .id(job.getId())
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

    private JobTechStackDocumentDto toJobTechStackEventDTO(JobTechStackRelation techStackRelation) {
        return JobTechStackDocumentDto.builder()
                .techStackId(techStackRelation.getTechStackId())
                .techStackName(techStackService.getById(techStackRelation.getTechStackId()).getName())
                .techStackExperienceLevel(techStackRelation.getExperienceLevel())
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
            log.warn("Exception during parsing JobIndexationDTO {} to JSON. {}", jobDocumentDto,  exception);
            return null;
        }
    }
}
