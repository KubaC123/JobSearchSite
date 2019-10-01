package getjobin.it.portal.jobservice.domain.company.boundary;

import getjobin.it.portal.jobservice.api.domain.CompanyDTO;
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
                .withWebSiteUrl(companyDTO.getWebSiteUrl())
                .withSize(companyDTO.getSize())
                .withLogoUrl(companyDTO.getLogoUrl())
                .withEstablishment(companyDTO.getEstablishment())
                .withDescription(companyDTO.getDescription())
                .withInstagramUrl(companyDTO.getInstagramUrl())
                .withFacebookUrl(companyDTO.getFacebookUrl())
                .withLinkedinUrl(companyDTO.getLinkedinUrl())
                .withTwitterUrl(companyDTO.getTwitterUrl())
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
                .webSiteUrl(company.getWebSiteUrl())
                .size(company.getSize())
                .logoUrl(company.getLogoUrl())
                .establishment(company.getEstablishment())
                .description(company.getDescription())
                .instagramUrl(company.getInstagramUrl())
                .facebookUrl(company.getFacebookUrl())
                .linkedinUrl(company.getLinkedinUrl())
                .twitterUrl(company.getTwitterUrl())
                .build();
    }

    public Company updateExistingEntity(Company existingEntity, CompanyDTO companyDTO) {
        Company.CompanyEntityBuilder builder = Company.toBuilder(existingEntity);
        return builder.withName(companyDTO.getName())
                .withWebSiteUrl(companyDTO.getWebSiteUrl())
                .withSize(companyDTO.getSize())
                .withLogoUrl(companyDTO.getLogoUrl())
                .withEstablishment(companyDTO.getEstablishment())
                .withDescription(companyDTO.getDescription())
                .withInstagramUrl(companyDTO.getInstagramUrl())
                .withFacebookUrl(companyDTO.getFacebookUrl())
                .withLinkedinUrl(companyDTO.getLinkedinUrl())
                .withTwitterUrl(companyDTO.getTwitterUrl())
                .build();
    }
}
