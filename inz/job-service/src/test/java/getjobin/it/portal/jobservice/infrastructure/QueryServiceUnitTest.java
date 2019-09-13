package getjobin.it.portal.jobservice.infrastructure;

import getjobin.it.portal.jobservice.domain.company.control.CompanyRepository;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.company.entity.TestCompanyBuilder;
import getjobin.it.portal.jobservice.infrastructure.query.QueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class QueryServiceUnitTest {

    @Autowired
    private QueryService queryService;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void givenExistingCompaniesIdsThenFindsIt() {
        List<Company> companies = TestCompanyBuilder.buildValidCompanies(20);
        List<Long> companiesIds = companies.stream()
                .map(companyRepository::saveCompany)
                .collect(Collectors.toList());
        List<Long> foundCompaniesIds = queryService.findEntitiesByIds(Company.class, companiesIds).stream()
                .map(Company::getId)
                .collect(Collectors.toList());
        companiesIds.forEach(id -> assertTrue(foundCompaniesIds.contains(id)));
    }
}
