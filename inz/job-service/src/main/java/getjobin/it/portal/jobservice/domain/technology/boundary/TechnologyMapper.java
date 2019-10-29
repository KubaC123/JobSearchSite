package getjobin.it.portal.jobservice.domain.technology.boundary;

import getjobin.it.portal.jobservice.api.TechnologyDto;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class TechnologyMapper {

    public Technology toEntity(TechnologyDto technologyDTO) {
        return Technology.builder()
                .withId(technologyDTO.getId())
                .withName(technologyDTO.getName())
                .withImageUrl(technologyDTO.getImageUrl())
                .build();
    }

    public List<TechnologyDto> toDtos(List<Technology> technologies) {
        return technologies.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TechnologyDto toDto(Technology technology) {
        return TechnologyDto.builder()
                .id(technology.getId())
                .name(technology.getName())
                .imageUrl(technology.getImageUrl())
                .build();
    }

    public Technology updateExistingTechnology(Technology existingTechnology, TechnologyDto technologyDto) {
        return Technology.toBuilder(existingTechnology)
                .withName(technologyDto.getName())
                .withImageUrl(technologyDto.getImageUrl())
                .build();
    }
}
