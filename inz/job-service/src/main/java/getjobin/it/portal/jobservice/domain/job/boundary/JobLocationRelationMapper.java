package getjobin.it.portal.jobservice.domain.job.boundary;

import getjobin.it.portal.jobservice.api.JobLocationDto;
import getjobin.it.portal.jobservice.domain.job.entity.JobLocationRelation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JobLocationRelationMapper {

    public List<JobLocationRelation> toEntities(Long jobId, List<JobLocationDto> jobLocationDtos) {
        return jobLocationDtos.stream()
                .map(jobTechStackDTO -> toEntity(Optional.ofNullable(jobId), jobTechStackDTO))
                .collect(Collectors.toList());
    }

    private JobLocationRelation toEntity(Optional<Long> jobId, JobLocationDto jobLocationDto) {
        return JobLocationRelation.builder()
                .jobId(jobId.orElse(null))
                .locationId(jobLocationDto.getLocation().getId())
                .remote(jobLocationDto.getRemote())
                .build();
    }
}
