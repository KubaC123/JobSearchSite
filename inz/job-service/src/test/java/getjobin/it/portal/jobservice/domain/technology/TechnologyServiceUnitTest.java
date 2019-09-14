package getjobin.it.portal.jobservice.domain.technology;

import getjobin.it.portal.jobservice.domain.job.TestJobBuilder;
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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TechnologyServiceUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private TechnologyService technologyService;

    @Autowired
    private JobService jobService;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(technologyService);
    }

    @Test
    public void givenValidDataThenCreatesTechnology() {
        Long createdTechnologyId = technologyService.createTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology createdTechnology = technologyService.getById(createdTechnologyId);
        assertEquals(createdTechnologyId, createdTechnology.getId());
    }

    @Test
    public void givenExistingTechStackThenFindsItById() {
        Long technologyId = technologyService.createTechnology(TestTechnologyBuilder.buildValidTechnology());
        Optional<Technology> foundTechnology = technologyService.findById(technologyId);
        assertTrue(foundTechnology.isPresent());
    }

    @Test
    public void givenValidDataOnUpdateThenUpdatesTechStack() {
        Long technologyId = technologyService.createTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology createdTechnology = technologyService.getById(technologyId);
        Technology updatedTechnology = TestTechnologyBuilder.buildValidUpdatedTechnology(createdTechnology);
        technologyService.updateTechnology(updatedTechnology);
        Technology finalTechnology = technologyService.getById(technologyId);
        assertEquals(TestTechnologyBuilder.NAME + UPDATE, finalTechnology.getName());
        assertEquals(TestTechnologyBuilder.IMAGE_URL + UPDATE, finalTechnology.getImageUrl());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenEmptyNameOnCreateThenThrowsConstraintViolationException() {
        technologyService.createTechnology(TestTechnologyBuilder.buildTechnologyWithEmptyName());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenNullNameOnCreateThenThrowsConstraintViolationException() {
        technologyService.createTechnology(TestTechnologyBuilder.buildTechnologyWithNullName());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenTechnologyWithActiveJobsOnRemoveThenThrowsConstraintViolationException() {
        Long technologyId = technologyService.createTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology createdTechnology = technologyService.getById(technologyId);
        jobService.createJob(TestJobBuilder.buildValidJobWithTechnology(createdTechnology));
        technologyService.removeTechnology(createdTechnology);
    }

    @Test
    public void givenTechnologyWithInactiveJobsThenRemovesIt() {
        Long technologyId = technologyService.createTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology createdTechnology = technologyService.getById(technologyId);
        Long createdJobId = jobService.createJob(TestJobBuilder.buildValidJobWithTechnology(createdTechnology));
        Job createdJob = jobService.getById(createdJobId);
        jobService.removeJob(createdJob);
        technologyService.removeTechnology(createdTechnology);
        Optional<Technology> removedTechnology = technologyService.findById(technologyId);
        assertTrue(removedTechnology.isEmpty());
    }
}
