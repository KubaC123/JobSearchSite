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

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CompanyServiceUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private CompanyService companyService;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(companyService);
    }

    @Test
    public void givenValidDataThenCreatesCompany() {
        Long companyId = companyService.createCompany(TestCompanyBuilder.buildValidCompany());
        assertNotNull(companyId);
    }

    @Test
    public void givenExistingCompanyThenRemovesIt() {
        Long companyId = companyService.createCompany(TestCompanyBuilder.buildValidCompany());
        Company createdCompany = companyService.getById(companyId);
        companyService.removeCompany(createdCompany);
        Optional<Company> removedCompany = companyService.findById(companyId);
        assertTrue(removedCompany.isEmpty());
    }

    @Test
    public void givenValidDataOnUpdateThenUpdatesCompany() {
        Long companyId = companyService.createCompany(TestCompanyBuilder.buildValidCompany());
        Company createdCompany = companyService.getById(companyId);
        Company updatedCompany = TestCompanyBuilder.buildValidUpdatedCompany(createdCompany);
        companyService.updateCompany(updatedCompany);
        Company finalCompany = companyService.getById(companyId);
        assertEquals(TestCompanyBuilder.NAME + UPDATE, finalCompany.getName());
        assertEquals(TestCompanyBuilder.WEBSITE + UPDATE, finalCompany.getWebSiteUrl());
        assertEquals(TestCompanyBuilder.SIZE, finalCompany.getSize());
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
