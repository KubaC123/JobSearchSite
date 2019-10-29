package getjobin.it.portal.jobservice.domain.techstack.boundary;

import getjobin.it.portal.jobservice.api.TechStackDto;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class TechStackMapper {

    public List<TechStack> toEntities(List<TechStackDto> techStackDtos) {
        return techStackDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public TechStack toEntity(TechStackDto techStackDTO) {
        return TechStack.builder()
                .withId(techStackDTO.getId())
                .withName(techStackDTO.getName())
                .build();
    }

    public List<TechStackDto> toDtos(List<TechStack> techStacks) {
        return techStacks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TechStackDto toDto(TechStack techStack) {
        return TechStackDto.builder()
                .id(techStack.getId())
                .name(techStack.getName())
                .build();
    }

    public TechStack updateExistingTechStack(TechStack existingTechStack, TechStackDto techStackDTO) {
        return TechStack.toBuilder(existingTechStack)
                .withName(techStackDTO.getName())
                .build();
    }
}
