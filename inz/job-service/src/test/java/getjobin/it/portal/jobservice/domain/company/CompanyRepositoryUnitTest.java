package getjobin.it.portal.jobservice.domain.company;


import getjobin.it.portal.jobservice.domain.company.control.CompanyRepository;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.company.entity.TestCompanyBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyRepositoryUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(companyRepository);
    }

    @Test
    @Transactional
    public void givenValidDataThenCreatesNewCompany() {
        Long companyId = companyRepository.saveCompany(TestCompanyBuilder.buildValidCompany());
        companyRepository.removeCompanyById(companyId);
        assertNotNull(companyId);
    }

    @Test
    @Transactional
    public void givenExistingCompanyThenFindsItById() {
        Long companyId = companyRepository.saveCompany(TestCompanyBuilder.buildValidCompany());
        Company foundCompany = companyRepository.getById(companyId);
        Long foundCompanyId = foundCompany.getId();
        companyRepository.removeCompany(foundCompany);
        assertEquals(companyId, foundCompanyId);
    }

    @Test
    @Transactional
    public void givenExistingCompanyRemovesIt() {
        Long companyId = companyRepository.saveCompany(TestCompanyBuilder.buildValidCompany());
        companyRepository.removeCompanyById(companyId);
        Optional<Company> removedCompany = companyRepository.findById(companyId);
        assertTrue(removedCompany.isEmpty());
    }

    @Test
    @Transactional
    public void givenValidDataOnUpdateThenUpdatesExistingCompany() {
        Long companyId = companyRepository.saveCompany(TestCompanyBuilder.buildValidCompany());
        Company createdCompany = companyRepository.getById(companyId);
        Company updatedCompany = TestCompanyBuilder.buildValidUpdatedCompany(createdCompany);
        companyRepository.updateCompany(updatedCompany);
        Company finalCompany = companyRepository.getById(companyId);
        companyRepository.removeCompany(finalCompany);
        assertEquals(TestCompanyBuilder.TEST_COMPANY_NAME + UPDATE, finalCompany.getName());
        assertEquals(TestCompanyBuilder.TEST_COMPANY_WEBSITE + UPDATE, finalCompany.getWebSite());
        assertEquals(TestCompanyBuilder.TEST_COMPANY_SIZE, finalCompany.getSize());
    }

}
