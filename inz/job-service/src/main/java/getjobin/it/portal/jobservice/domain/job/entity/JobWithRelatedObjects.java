package getjobin.it.portal.jobservice.domain.job.entity;

import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.util.Pair;

import java.util.List;

@Builder
@Getter
public class JobWithRelatedObjects {

    private Job job;
    private List<Pair<TechStack, Integer>> techStacksWithExperienceLevel;
    private List<Pair<Location, Boolean>> locationsWithRemote;
}
