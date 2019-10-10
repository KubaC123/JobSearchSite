package getjobin.it.portal.jobservice.domain.job.boundary;

import getjobin.it.portal.jobservice.api.JobTechStackDto;
import getjobin.it.portal.jobservice.domain.job.entity.JobTechStackRelation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JobTechStackRelationMapper {

    public List<JobTechStackRelation> toEntities(Long jobOfferId, List<JobTechStackDto> jobTechStackDtos) {
        return jobTechStackDtos.stream()
                .map(jobTechStackDTO -> toEntity(Optional.ofNullable(jobOfferId), jobTechStackDTO))
                .collect(Collectors.toList());
    }

    private JobTechStackRelation toEntity(Optional<Long> jobOfferId, JobTechStackDto jobTechStackDto) {
        return JobTechStackRelation.builder()
                .withJobId(jobOfferId.orElse(null))
                .withTechStackId(jobTechStackDto.getTechStack().getId())
                .withExperienceLevel(jobTechStackDto.getExperienceLevel())
                .build();
    }
}
