package getjobin.it.portal.jobservice.domain.company;

import getjobin.it.portal.jobservice.domain.company.control.CompanyService;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.company.entity.TestCompanyBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyServiceUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private CompanyService companyService;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(companyService);
    }

    @Test
    @Transactional
    public void givenValidDataThenCreatesNewCompany() {
        Long companyId = companyService.createCompany(TestCompanyBuilder.buildValidCompany());
        Company createdCompany = companyService.getById(companyId);
        companyService.removeCompany(createdCompany);
        assertNotNull(companyId);
    }


    @Test
    @Transactional
    public void givenExistingCompanyRemovesIt() {
        Long companyId = companyService.createCompany(TestCompanyBuilder.buildValidCompany());
        Company createdCompany = companyService.getById(companyId);
        companyService.removeCompany(createdCompany);
        Optional<Company> removedCompany = companyService.findById(companyId);
        assertTrue(removedCompany.isEmpty());
    }

    @Test
    @Transactional
    public void givenValidDataOnUpdateThenUpdatesExistingCompany() {
        Long companyId = companyService.createCompany(TestCompanyBuilder.buildValidCompany());
        Company createdCompany = companyService.getById(companyId);
        Company updatedCompany = TestCompanyBuilder.buildValidUpdatedCompany(createdCompany);
        companyService.updateCompany(updatedCompany);
        Company finalCompany = companyService.getById(companyId);
        companyService.removeCompany(finalCompany);
        assertEquals(TestCompanyBuilder.TEST_COMPANY_NAME + UPDATE, finalCompany.getName());
        assertEquals(TestCompanyBuilder.TEST_COMPANY_WEBSITE + UPDATE, finalCompany.getWebSiteUrl());
        assertEquals(TestCompanyBuilder.TEST_COMPANY_SIZE, finalCompany.getSize());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenNullNameOnCreateThenThrowsConstraintViolationException() {
        companyService.createCompany(TestCompanyBuilder.buildCompanyWithNullName());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenEmptyNameOnCreateThenThrowsConstraintViolationException() {
        companyService.createCompany(TestCompanyBuilder.buildCompanyWithEmptyName());
    }

}
