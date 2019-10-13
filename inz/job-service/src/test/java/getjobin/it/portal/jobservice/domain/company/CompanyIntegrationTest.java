package getjobin.it.portal.jobservice.domain.company;

import getjobin.it.portal.jobservice.domain.IntegrationTest;
import getjobin.it.portal.jobservice.domain.company.control.CompanyService;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.company.entity.TestCompanyBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CompanyIntegrationTest {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private IntegrationTest integrationTest;

    @Before
    public void init() {
        integrationTest.init();
    }

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(companyService);
        assertNotNull(integrationTest);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenCompanyWithActiveJobsOnRemoveThenThrowsConstraintViolationException() {
        Long companyId = companyService.create(TestCompanyBuilder.buildValidCompany());
        Company createdCompany = companyService.getById(companyId);
        integrationTest.createValidJobWith(createdCompany);
        companyService.remove(createdCompany);
    }
}
