package getjobin.it.portal.jobservice.domain.company.boundary;

import getjobin.it.portal.jobservice.api.CompanyDto;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class CompanyMapper {

    public Company toEntity(CompanyDto companyDTO) {
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

    public List<CompanyDto> toDtos(List<Company> companyEntities) {
        return companyEntities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CompanyDto toDto(Company company) {
        return CompanyDto.builder()
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

    public Company updateExistingEntity(Company existingEntity, CompanyDto companyDTO) {
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
