package getjobin.it.portal.jobservice.domain.company;

import getjobin.it.portal.jobservice.api.CompanyDTO;
import getjobin.it.portal.jobservice.domain.CompanyEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMapper {

    public List<CompanyEntity> fromDTOtoEntity(List<CompanyDTO> companyDTOs) {
        return companyDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public CompanyEntity toEntity(CompanyDTO companyDTO) {
        return CompanyEntity.builder()
                .withId(companyDTO.getId())
                .withName(companyDTO.getName())
                .withWebSite(companyDTO.getWebSite())
                .withSize(companyDTO.getSize())
                .withLogoPath(companyDTO.getLogoPath())
                .build();

    }

    public List<CompanyDTO> fromEntityToDTO(List<CompanyEntity> companyEntities) {
        return companyEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CompanyDTO toDTO(CompanyEntity companyEntity) {
        return CompanyDTO.builder()
                .id(companyEntity.getId())
                .name(companyEntity.getName())
                .webSite(companyEntity.getWebSite())
                .size(companyEntity.getSize())
                .logoPath(companyEntity.getLogoPath())
                .build();
    }

    public CompanyEntity updateExistingEntity(CompanyEntity existingEntity, CompanyDTO companyDTO) {
        CompanyEntity.CompanyEntityBuilder builder = CompanyEntity.toBuilder(existingEntity);
        return builder.withName(companyDTO.getName())
                .withWebSite(companyDTO.getWebSite())
                .withSize(companyDTO.getSize())
                .withLogoPath(companyDTO.getLogoPath())
                .build();
    }
}
