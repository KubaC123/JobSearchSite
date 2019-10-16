package getjobin.it.portal.jobservice.domain.search;

import getjobin.it.portal.jobservice.domain.company.control.CompanyRepository;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.company.entity.TestCompanyBuilder;
import getjobin.it.portal.jobservice.domain.search.boundary.QueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                .map(companyRepository::save)
                .collect(Collectors.toList());
        List<Long> foundCompaniesIds = queryService.queryByIds(companiesIds, Company.class).stream()
                .map(Company::getId)
                .collect(Collectors.toList());
        companiesIds.forEach(id -> assertTrue(foundCompaniesIds.contains(id)));
    }

    @Test
    public void givenIdsOfNonExistingEntitiesThenReturnsEmptyList() {
        List<Long> unusedIds = Stream.of(-1L, -2L, -3L, -4L).collect(Collectors.toList());
        List<Company> foundCompanies = queryService.queryByIds(unusedIds, Company.class);
        assertTrue(foundCompanies.isEmpty());
    }
}
