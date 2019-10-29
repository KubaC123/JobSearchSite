package getjobin.it.portal.jobservice.domain.job.boundary;

import getjobin.it.portal.jobservice.api.JobLocationDto;
import getjobin.it.portal.jobservice.domain.job.entity.JobLocationRelation;
import getjobin.it.portal.jobservice.domain.location.boundary.LocationMapper;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class JobLocationRelationMapper {

    @Autowired
    private LocationMapper locationMapper;

    public List<JobLocationDto> toDtos(List<Pair<Location, Boolean>> locationsWithRemote) {
        return locationsWithRemote.stream()
                .map(pair -> JobLocationDto.builder()
                        .location(locationMapper.toDto(pair.getFirst()))
                        .remote(pair.getSecond())
                        .build())
                .collect(Collectors.toList());
    }

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
