package getjobin.it.portal.jobservice.domain.technology.boundary;

import getjobin.it.portal.jobservice.api.TechnologyDto;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import org.springframework.stereotype.Component;

@Component
public class TechnologyMapper {

    public Technology toEntity(TechnologyDto technologyDTO) {
        return Technology.builder()
                .withId(technologyDTO.getId())
                .withName(technologyDTO.getName())
                .withImageUrl(technologyDTO.getImageUrl())
                .build();
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
