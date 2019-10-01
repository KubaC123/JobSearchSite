package getjobin.it.portal.jobservice.domain.technology;

import getjobin.it.portal.jobservice.domain.job.entity.TestJobBuilder;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.technology.control.TechnologyService;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.technology.entity.TestTechnologyBuilder;
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
public class TechnologyIntegrationTest {

    @Autowired
    private TechnologyService technologyService;

    @Autowired
    private JobService jobService;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(technologyService);
        assertNotNull(jobService);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenTechnologyWithActiveJobsOnRemoveThenThrowsConstraintViolationException() {
        Long technologyId = technologyService.createTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology createdTechnology = technologyService.getById(technologyId);
        jobService.create(TestJobBuilder.buildValidJobWithTechnology(createdTechnology));
        technologyService.remove(createdTechnology);
    }

    @Test
    public void givenTechnologyWithInactiveJobsThenRemovesIt() {
        Long technologyId = technologyService.createTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology createdTechnology = technologyService.getById(technologyId);
        Long createdJobId = jobService.create(TestJobBuilder.buildValidJobWithTechnology(createdTechnology));
        Job createdJob = jobService.getById(createdJobId);
        jobService.remove(createdJob);
        technologyService.remove(createdTechnology);
        Optional<Technology> removedTechnology = technologyService.findById(technologyId);
        assertTrue(removedTechnology.isEmpty());
    }
}
