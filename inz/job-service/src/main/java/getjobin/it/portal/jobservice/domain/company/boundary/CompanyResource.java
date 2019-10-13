package getjobin.it.portal.jobservice.domain.company.boundary;

import getjobin.it.portal.jobservice.api.CompanyDto;
import getjobin.it.portal.jobservice.api.ResourceDto;
import getjobin.it.portal.jobservice.domain.company.control.CompanyService;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.infrastructure.util.IdsParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = CompanyResource.COMPANY_PATH)
@Slf4j
public class CompanyResource {

    public static final String COMPANY_PATH = "company";

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyService companyService;

    @RequestMapping(method = RequestMethod.GET, value = IdsParam.IDS_PATH)
    public List<CompanyDto> browseCompanies(@PathVariable(IdsParam.IDS) IdsParam ids) {
        return companyService.findByIds(ids.asList()).stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResourceDto createCompany(@RequestBody CompanyDto companyDTO) {
        Long newCompanyId = companyService.create(companyMapper.toEntity(companyDTO));
        return buildResourceDTO(newCompanyId);
    }

    private ResourceDto buildResourceDTO(Long companyId) {
        return ResourceDto.builder()
                .objectType(Company.COMPANY_TYPE)
                .objectId(companyId)
                .resourceURI(ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/" + companyId)
                        .build()
                        .toUri())
                .build();
    }

    @RequestMapping(method = RequestMethod.PUT, value = IdsParam.ID_PATH)
    public ResourceDto updateCompany(@PathVariable(IdsParam.ID) Long companyId, @RequestBody CompanyDto companyDto) {
        Company foundCompany = companyService.getById(companyId);
        Company updatedCompany = companyMapper.updateExistingEntity(foundCompany, companyDto);
        companyService.update(updatedCompany);
        return buildResourceDTO(updatedCompany.getId());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = IdsParam.IDS_PATH)
    public void deleteCompanies(@PathVariable(IdsParam.IDS) IdsParam ids) {
        List<Company> foundCompanies = companyService.findByIds(ids.asList());
        log.info(MessageFormat.format("[COMPANY] removing companies with ids: {0}", getCommaSeparatedIds(foundCompanies)));
        foundCompanies.forEach(companyService::remove);
    }

    private String getCommaSeparatedIds(List<Company> companies) {
        return companies.stream()
                .map(Company::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

}
