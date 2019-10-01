package getjobin.it.portal.jobservice.domain.job;

import getjobin.it.portal.jobservice.domain.company.control.CompanyService;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.company.entity.TestCompanyBuilder;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.TestJobBuilder;
import getjobin.it.portal.jobservice.domain.technology.control.TechnologyService;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.technology.entity.TestTechnologyBuilder;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.domain.techstack.entity.TestTechStackBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JobServiceIntegrationTest {

    @Autowired
    private JobService jobService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private TechnologyService technologyService;

    @Autowired
    private TechStackService techStackService;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(jobService);
        assertNotNull(techStackService);
    }

    @Test
    public void givenValidJobWithCompanyThenCreatesIt() {
        Long companyId = companyService.createCompany(TestCompanyBuilder.buildValidCompany());
        Company createdCompany = companyService.getById(companyId);
        Long jobId = jobService.create(TestJobBuilder.buildValidJobInCompany(createdCompany));
        Job createdJob = jobService.getById(jobId);
        assertEquals(createdCompany, createdJob.getCompany());
    }

    @Test
    public void givenValidJobWithTechnologyThenCreatesIt() {
        Long technologyId = technologyService.createTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology createdTechnology = technologyService.getById(technologyId);
        Long jobId = jobService.create(TestJobBuilder.buildValidJobWithTechnology(createdTechnology));
        Job createdJob = jobService.getById(jobId);
        assertEquals(createdTechnology, createdJob.getTechnology());
    }

    // todo fix this test
    @Test
    public void givenValidJobWithTechStackThenCreatesJobWithTechStackRelation() {
        Long techStackId = techStackService.create(TestTechStackBuilder.buildValidTechStack());
        TechStack createdTechStack = techStackService.getById(techStackId);
        Long jobId = jobService.create(TestJobBuilder.buildValidJobWithTechStack(createdTechStack));
        Job createdJob = jobService.getById(jobId);
        assertTrue(createdJob.getTechStackRelations().isPresent());
        assertEquals(1, createdJob.getTechStackRelations().get().size());
        assertEquals(jobId, createdJob.getTechStackRelations().get().get(0).getJobId());
        assertEquals(techStackId, createdJob.getTechStackRelations().get().get(0).getTechStackId());
        assertEquals(TestJobBuilder.TECH_STACK_RELATION_EXP_LEVEL, createdJob.getTechStackRelations().get().get(0).getExperienceLevel());
    }
}
