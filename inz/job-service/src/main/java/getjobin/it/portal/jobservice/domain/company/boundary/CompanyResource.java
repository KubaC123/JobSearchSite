package getjobin.it.portal.jobservice.domain.company.boundary;


import getjobin.it.portal.jobservice.api.CompanyDTO;
import getjobin.it.portal.jobservice.api.ResourceDTO;
import getjobin.it.portal.jobservice.domain.company.control.CompanyMapper;
import getjobin.it.portal.jobservice.domain.company.control.CompanyService;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.infrastructure.IdsParam;
import getjobin.it.portal.jobservice.infrastructure.exceptions.JobServicePreconditions;
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
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = CompanyResource.MAIN_PATH)
@Slf4j
public class CompanyResource {

    public static final String MAIN_PATH = "company";
    private static final String IDS_PATH = "{ids}";
    private static final String IDS = "ids";

    private CompanyMapper companyMapper;
    private CompanyService companyService;

    @Autowired
    public CompanyResource(CompanyMapper companyMapper, CompanyService companyService) {
        this.companyMapper = companyMapper;
        this.companyService = companyService;
    }

    @RequestMapping(method = RequestMethod.GET, value = IDS_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public List<CompanyDTO> browseCompanies(@PathVariable(IDS) IdsParam ids) {
        return companyService.findByIds(ids.asList()).stream()
                .map(companyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResourceDTO createCompany(@RequestBody CompanyDTO companyDTO) {
        Long newCompanyId = companyService.createCompany(companyMapper.toEntity(companyDTO));
        return buildResourceDTO(newCompanyId);
    }

    private ResourceDTO buildResourceDTO(Long companyId) {
        return ResourceDTO.builder()
                .objectType(Company.COMPANY_TYPE)
                .objectId(companyId)
                .resourceURI(ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/" + companyId)
                        .build()
                        .toUri())
                .build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public ResourceDTO updateCompany(@RequestBody CompanyDTO companyDTO) {
        JobServicePreconditions.checkArgument(Objects.nonNull(companyDTO.getId()), "Specify company id in order to update it");
        Company foundCompany = companyService.getById(companyDTO.getId());
        Company updatedCompany = companyMapper.updateExistingEntity(foundCompany, companyDTO);
        companyService.updateCompany(updatedCompany);
        return buildResourceDTO(updatedCompany.getId());
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteCompanies(@PathVariable(IDS) IdsParam ids) {
        List<Company> foundCompanies = companyService.findByIds(ids.asList());
        log.info(MessageFormat.format("[COMPANY] removing companies with ids: {0}", getCommaSeparatedIds(foundCompanies)));
        foundCompanies.forEach(companyService::removeCompany);
    }

    private String getCommaSeparatedIds(List<Company> companies) {
        return companies.stream()
                .map(Company::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

}
