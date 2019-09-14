package getjobin.it.portal.jobservice.domain.job.control;

import getjobin.it.portal.jobservice.api.JobTechStackDTO;
import getjobin.it.portal.jobservice.domain.job.entity.JobTechStackRelation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JobTechStackRelationMapper {

    public List<JobTechStackRelation> toEntities(Long jobOfferId, List<JobTechStackDTO> jobTechStackDTOs) {
        return jobTechStackDTOs.stream()
                .map(jobTechStackDTO -> toEntity(Optional.ofNullable(jobOfferId), jobTechStackDTO))
                .collect(Collectors.toList());
    }

    private JobTechStackRelation toEntity(Optional<Long> jobOfferId, JobTechStackDTO jobTechStackDTO) {
        return JobTechStackRelation.builder()
                .withJobId(jobOfferId.orElse(null))
                .withTechStackId(jobTechStackDTO.getTechStack().getId())
                .withExperienceLevel(jobTechStackDTO.getExperienceLevel())
                .build();
    }
}
