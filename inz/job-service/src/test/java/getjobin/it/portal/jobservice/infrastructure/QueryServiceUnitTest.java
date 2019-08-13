package getjobin.it.portal.jobservice.infrastructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import getjobin.it.portal.jobservice.domain.CompanyEntity;
import getjobin.it.portal.jobservice.domain.company.CompanyRepository;
import getjobin.it.portal.jobservice.infrastructure.query.QueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.nio.charset.CoderMalfunctionError;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryServiceUnitTest {

    @Autowired
    private QueryService queryService;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void whenSpecifiedIdsOfExistingCompaniesThenFindsIt() {
        CompanyEntity firstCompany = CompanyEntity.builder()
                .withName("Allegro")
                .withWebSite("www.allegro.pl")
                .withSize("500")
                .build();
        CompanyEntity secondCompany = CompanyEntity.builder()
                .withName("OLX")
                .withWebSite("www.olx.pl")
                .withSize("400")
                .build();
        Long firstCompanyId = companyRepository.createCompany(firstCompany);
        Long secondCompanyId = companyRepository.createCompany(secondCompany);
        List<Long> foundCompaniesIds = queryService.findEntitiesByIds(CompanyEntity.class, Arrays.asList(firstCompanyId, secondCompanyId)).stream()
                .map(CompanyEntity::getId)
                .collect(Collectors.toList());
        companyRepository.removeCompanyById(firstCompanyId);
        companyRepository.removeCompanyById(secondCompanyId);
        assertTrue(foundCompaniesIds.contains(firstCompanyId));
        assertTrue(foundCompaniesIds.contains(secondCompanyId));
    }

}
