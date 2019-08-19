package getjobin.it.portal.jobservice.domain.company.control;

import getjobin.it.portal.jobservice.api.CompanyDTO;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMapper {

    public List<Company> fromDTOtoEntity(List<CompanyDTO> companyDTOs) {
        return companyDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public Company toEntity(CompanyDTO companyDTO) {
        return Company.builder()
                .withId(companyDTO.getId())
                .withName(companyDTO.getName())
                .withWebSite(companyDTO.getWebSite())
                .withSize(companyDTO.getSize())
                .withLogoPath(companyDTO.getLogoPath())
                .withEstablishment(companyDTO.getEstablishment())
                .withDescription(companyDTO.getDescription())
                .build();

    }

    public List<CompanyDTO> fromEntityToDTO(List<Company> companyEntities) {
        return companyEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CompanyDTO toDTO(Company company) {
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .webSite(company.getWebSite())
                .size(company.getSize())
                .logoPath(company.getLogoPath())
                .establishment(company.getEstablishment())
                .description(company.getDescription())
                .build();
    }

    public Company updateExistingEntity(Company existingEntity, CompanyDTO companyDTO) {
        Company.CompanyEntityBuilder builder = Company.toBuilder(existingEntity);
        return builder.withName(companyDTO.getName())
                .withWebSite(companyDTO.getWebSite())
                .withSize(companyDTO.getSize())
                .withLogoPath(companyDTO.getLogoPath())
                .withEstablishment(companyDTO.getEstablishment())
                .withDescription(companyDTO.getDescription())
                .build();
    }
}
