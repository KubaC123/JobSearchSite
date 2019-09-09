package getjobin.it.portal.jobservice.domain.techstack.control;

import getjobin.it.portal.jobservice.api.TechStackDTO;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TechStackMapper {

    public List<TechStack> toEntities(List<TechStackDTO> techStackDTOs) {
        return techStackDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public TechStack toEntity(TechStackDTO techStackDTO) {
        return TechStack.builder()
                .withId(techStackDTO.getId())
                .withName(techStackDTO.getName())
                .build();
    }

    public List<TechStackDTO> toDTOs(List<TechStack> techStacks) {
        return techStacks.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TechStackDTO toDTO(TechStack techStack) {
        return TechStackDTO.builder()
                .id(techStack.getId())
                .name(techStack.getName())
                .build();
    }

    public TechStack updateExistingTechStack(TechStack existingTechStack, TechStackDTO techStackDTO) {
        return TechStack.toBuilder(existingTechStack)
                .withName(techStackDTO.getName())
                .build();
    }
}
