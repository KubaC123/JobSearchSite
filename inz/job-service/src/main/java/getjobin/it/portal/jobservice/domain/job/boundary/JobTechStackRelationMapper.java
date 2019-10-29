package getjobin.it.portal.jobservice.domain.job.boundary;

import getjobin.it.portal.jobservice.api.JobTechStackDto;
import getjobin.it.portal.jobservice.domain.job.entity.JobTechStackRelation;
import getjobin.it.portal.jobservice.domain.techstack.boundary.TechStackMapper;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class JobTechStackRelationMapper {

    @Autowired
    private TechStackMapper techStackMapper;

    public List<JobTechStackDto> toDtos(List<Pair<TechStack, Integer>> techStacksWithExperienceLevel) {
        return techStacksWithExperienceLevel.stream()
                .map(pair -> JobTechStackDto.builder()
                        .techStack(techStackMapper.toDto(pair.getFirst()))
                        .experienceLevel(pair.getSecond())
                        .build())
                .collect(Collectors.toList());
    }

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
