package getjobin.it.portal.jobservice.domain.company;

import getjobin.it.portal.jobservice.domain.company.control.CompanyService;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.company.entity.TestCompanyBuilder;
import getjobin.it.portal.jobservice.domain.job.entity.TestJobBuilder;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CompanyIntegrationTest {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JobService jobService;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(companyService);
        assertNotNull(jobService);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenCompanyWithActiveJobsOnRemoveThenThrowsConstraintViolationException() {
        Long companyId = companyService.createCompany(TestCompanyBuilder.buildValidCompany());
        Company createdCompany = companyService.getById(companyId);
        jobService.createJob(TestJobBuilder.buildValidJobInCompany(createdCompany));
        companyService.removeCompany(createdCompany);
    }

    @Test
    public void givenCompanyWithInactiveJobsThenRemovesIt() {
        Long companyId = companyService.createCompany(TestCompanyBuilder.buildValidCompany());
        Company createdCompany = companyService.getById(companyId);
        Long companyJobId = jobService.createJob(TestJobBuilder.buildValidJobInCompany(createdCompany));
        Job companyJob = jobService.getById(companyJobId);
        jobService.removeJob(companyJob);
        companyService.removeCompany(createdCompany);
        Optional<Company> removedCompany = companyService.findById(companyId);
        assertTrue(removedCompany.isEmpty());
    }
}
