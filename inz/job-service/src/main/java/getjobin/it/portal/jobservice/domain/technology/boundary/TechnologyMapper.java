package getjobin.it.portal.jobservice.domain.technology.boundary;

import getjobin.it.portal.jobservice.api.domain.rest.TechnologyDTO;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import org.springframework.stereotype.Component;

@Component
public class TechnologyMapper {

    public Technology toEntity(TechnologyDTO technologyDTO) {
        return Technology.builder()
                .withId(technologyDTO.getId())
                .withName(technologyDTO.getName())
                .withImageUrl(technologyDTO.getImageUrl())
                .build();
    }

    public TechnologyDTO toDTO(Technology technology) {
        return TechnologyDTO.builder()
                .id(technology.getId())
                .name(technology.getName())
                .imageUrl(technology.getImageUrl())
                .build();
    }

    public Technology updateExistingTechnology(Technology existingTechnology, TechnologyDTO technologyDTO) {
        return Technology.toBuilder(existingTechnology)
                .withName(technologyDTO.getName())
                .withImageUrl(technologyDTO.getImageUrl())
                .build();
    }
}
