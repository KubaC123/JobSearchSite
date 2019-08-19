package getjobin.it.portal.jobservice.domain;


import getjobin.it.portal.jobservice.domain.company.control.CompanyRepository;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyRepositoryUnitTest {

    public static final String TEST_COMPANY_NAME = "Cichy kącik";
    public static final String TEST_COMPANY_WEBSITE = "www.cichykacik.pl";
    public static final String TEST_COMPANY_SIZE = "1";
    private static final String UPDATE = "update";

    @Autowired
    private CompanyRepository companyRepository;

    private Company validCompany;

    @Before
    public void buildTestCompanies() {
        validCompany = buildValidCompany();
    }

    public Company buildValidCompany() {
        return Company.builder()
                .withName(TEST_COMPANY_NAME)
                .withWebSite(TEST_COMPANY_WEBSITE)
                .withSize(TEST_COMPANY_SIZE)
                .build();
    }

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(companyRepository);
    }

    @Test
    @Transactional
    public void givenValidDataThenCreatesNewCompany() {
        Long companyId = companyRepository.saveCompany(validCompany);
        companyRepository.removeCompanyById(companyId);
        assertNotNull(companyId);
    }

    @Test
    @Transactional
    public void givenExistingCompanyThenFindsItById() {
        Long companyId = companyRepository.saveCompany(validCompany);
        Company foundCompany = companyRepository.getById(companyId);
        Long foundCompanyId = foundCompany.getId();
        companyRepository.removeCompany(foundCompany);
        assertEquals(companyId, foundCompanyId);
    }

    @Test
    @Transactional
    public void givenExistingCompanyRemovesIt() {
        Long companyId = companyRepository.saveCompany(validCompany);
        companyRepository.removeCompanyById(companyId);
        Optional<Company> removedCompany = companyRepository.findById(companyId);
        assertTrue(removedCompany.isEmpty());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenNullNameOnCreateThenThrowsConstraintViolationException() {
        companyRepository.saveCompany(Company.builder()
                .withWebSite(TEST_COMPANY_WEBSITE)
                .withSize(TEST_COMPANY_SIZE)
                .build());
    }

    @Test
    @Transactional
    public void givenValidDataOnUpdateThenUpdatesExistingCompany() {
        Long companyId = companyRepository.saveCompany(validCompany);
        Company createdCompany = companyRepository.getById(companyId);
        Company updatedCompany = buildUpdatedCompany(createdCompany);
        companyRepository.updateCompany(updatedCompany);
        Company finalCompany = companyRepository.getById(companyId);
        companyRepository.removeCompany(finalCompany);
        assertEquals(TEST_COMPANY_NAME + UPDATE, finalCompany.getName());
        assertEquals(TEST_COMPANY_WEBSITE + UPDATE, finalCompany.getWebSite());
        assertEquals(TEST_COMPANY_SIZE, finalCompany.getSize());
    }

    private Company buildUpdatedCompany(Company createdCompany) {
        return Company.toBuilder(createdCompany)
                .withName(TEST_COMPANY_NAME + UPDATE)
                .withWebSite(TEST_COMPANY_WEBSITE + UPDATE)
                .build();
    }
}
