package getjobin.it.portal.jobservice.domain.joboffer.control;

import getjobin.it.portal.jobservice.api.JobTechStackDTO;
import getjobin.it.portal.jobservice.domain.joboffer.entity.JobTechStackRelation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobTechStackRelationMapper {

    public List<JobTechStackRelation> toEntities(Long jobOfferId, List<JobTechStackDTO> jobTechStackDTOs) {
        return jobTechStackDTOs.stream()
                .map(jobTechStackDTO -> toEntity(jobOfferId, jobTechStackDTO))
                .collect(Collectors.toList());
    }

    private JobTechStackRelation toEntity(Long jobOfferId, JobTechStackDTO jobTechStackDTO) {
        return JobTechStackRelation.builder()
                .withJobOfferId(jobOfferId)
                .withTechStackId(jobTechStackDTO.getTechStack().getId())
                .withExperienceLevel(jobTechStackDTO.getExperienceLevel())
                .build();
    }
}
