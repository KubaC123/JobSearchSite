package getjobin.it.portal.jobservice.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import getjobin.it.portal.jobservice.domain.company.CompanyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyRepositoryUnitTest {

    private static final String TEST_COMPANY_NAME = "Cichy kącik";
    private static final String TEST_COMPANY_WEBSITE = "www.cichykacik.pl";
    private static final String TEST_COMPANY_SIZE = "1";
    private static final String UPDATE = "update";

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(companyRepository);
    }

    @Test
    @Transactional
    public void givenValidDataOnCreateThenCreatesNewCompanyAndFindsItById() {
        Long companyId = createValidCompany();
        CompanyEntity foundCompany = companyRepository.findById(companyId);
        Long foundCompanyId = foundCompany.getId();
        companyRepository.removeCompany(foundCompany);
        assertEquals(companyId, foundCompanyId);
    }

    public Long createValidCompany() {
        return companyRepository.createCompany(CompanyEntity.builder()
                .withName(TEST_COMPANY_NAME)
                .withWebSite(TEST_COMPANY_WEBSITE)
                .withSize(TEST_COMPANY_SIZE)
                .build());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenEmptyNameOnCreateThenThrowsConstraintViolationException() {
        createCompanyWithEmptyName();
    }

    public Long createCompanyWithEmptyName() {
        return companyRepository.createCompany(CompanyEntity.builder()
                .withWebSite(TEST_COMPANY_WEBSITE)
                .withSize(TEST_COMPANY_SIZE)
                .build());
    }

    @Test
    @Transactional
    public void givenValidDataOnUpdateThenUpdatesExistingCompany() {
        Long companyId = createValidCompany();
        CompanyEntity createdCompany = companyRepository.findById(companyId);
        CompanyEntity updatedCompany = CompanyEntity.toBuilder(createdCompany)
                .withName(TEST_COMPANY_NAME + UPDATE)
                .withWebSite(TEST_COMPANY_WEBSITE + UPDATE)
                .build();
        companyRepository.updateCompany(updatedCompany);
        CompanyEntity finalCompany = companyRepository.findById(companyId);
        assertEquals(TEST_COMPANY_NAME + UPDATE, finalCompany.getName());
        assertEquals(TEST_COMPANY_WEBSITE + UPDATE, finalCompany.getWebSite());
        assertEquals(TEST_COMPANY_SIZE, finalCompany.getSize());
        companyRepository.removeCompany(finalCompany);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenEmptyNameOnUpdateThenThrowsConstraintViolationException() {
        Long companyId = createValidCompany();
        CompanyEntity foundCompany = companyRepository.findById(companyId);
        CompanyEntity updatedCompany = CompanyEntity.toBuilder(foundCompany)
                .withName(null)
                .build();
        companyRepository.updateCompany(updatedCompany);
    }

}
