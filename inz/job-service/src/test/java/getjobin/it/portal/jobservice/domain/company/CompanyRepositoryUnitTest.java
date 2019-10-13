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
@Transactional
public class CompanyRepositoryUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(companyRepository);
    }

    @Test
    public void givenValidDataThenCreatesCompany() {
        Long companyId = companyRepository.save(TestCompanyBuilder.buildValidCompany());
        assertNotNull(companyId);
    }

    @Test
    public void givenExistingCompanyThenFindsItById() {
        Long companyId = companyRepository.save(TestCompanyBuilder.buildValidCompany());
        Company foundCompany = companyRepository.getById(companyId);
        Long foundCompanyId = foundCompany.getId();
        assertEquals(companyId, foundCompanyId);
    }

    @Test
    public void givenExistingCompanyRemovesIt() {
        Long companyId = companyRepository.save(TestCompanyBuilder.buildValidCompany());
        companyRepository.removeById(companyId);
        Optional<Company> removedCompany = companyRepository.findById(companyId);
        assertTrue(removedCompany.isEmpty());
    }

    @Test
    public void givenValidDataOnUpdateThenUpdatesCompany() {
        Long companyId = companyRepository.save(TestCompanyBuilder.buildValidCompany());
        Company createdCompany = companyRepository.getById(companyId);
        Company updatedCompany = TestCompanyBuilder.buildValidUpdatedCompany(createdCompany);
        companyRepository.update(updatedCompany);
        Company finalCompany = companyRepository.getById(companyId);
        assertEquals(TestCompanyBuilder.NAME + UPDATE, finalCompany.getName());
        assertEquals(TestCompanyBuilder.WEBSITE + UPDATE, finalCompany.getWebSiteUrl());
        assertEquals(TestCompanyBuilder.SIZE, finalCompany.getSize());
    }

}
