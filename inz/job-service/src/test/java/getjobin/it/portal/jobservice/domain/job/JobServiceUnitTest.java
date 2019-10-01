package getjobin.it.portal.jobservice.domain.job;

import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.TestJobBuilder;
import getjobin.it.portal.jobservice.infrastructure.exception.JobServiceException;
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
@Transactional
public class JobServiceUnitTest {

    @Autowired
    private JobService jobService;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(jobService);
    }

    @Test
    public void givenValidDataThenCreatesJob(){
        Long createdJobId = jobService.createJob(TestJobBuilder.buildValidJob());
        Job createdJob = jobService.getById(createdJobId);
        assertNotNull(createdJobId);
        assertEquals(TestJobBuilder.TYPE, createdJob.getType());
        assertEquals(TestJobBuilder.TITLE, createdJob.getTitle());
        assertEquals(TestJobBuilder.EXP_LEVEL, createdJob.getExperienceLevel());
        assertEquals(TestJobBuilder.EMP_TYPE, createdJob.getEmploymentType());
        assertEquals(TestJobBuilder.SALARY_MIN, createdJob.getSalaryMin());
        assertEquals(TestJobBuilder.SALARY_MAX, createdJob.getSalaryMax());
        assertEquals(TestJobBuilder.START_DATE, createdJob.getStartDate());
        assertEquals(TestJobBuilder.CONTRACT_DURATION, createdJob.getContractDuration());
        assertEquals(TestJobBuilder.FLEXIBLE_WORK_HOURS, createdJob.getFlexibleWorkHours());
        assertEquals(TestJobBuilder.CURRENCY, createdJob.getCurrency());
        assertEquals(TestJobBuilder.DESCRIPTION, createdJob.getDescription());
        assertEquals(TestJobBuilder.PROJECT_INDUSTRY, createdJob.getProjectIndustry());
        assertEquals(TestJobBuilder.PROJECT_TEAM_SIZE, createdJob.getProjectTeamSize());
        assertEquals(TestJobBuilder.PROJECT_DESCRIPTION, createdJob.getProjectDescription());
        assertEquals(TestJobBuilder.DEVELOPMENT, createdJob.getDevelopment());
        assertEquals(TestJobBuilder.MAINTENANCE, createdJob.getMaintenance());
        assertEquals(TestJobBuilder.TESTING, createdJob.getTesting());
        assertEquals(TestJobBuilder.CLIENT_SUPPORT, createdJob.getClientSupport());
        assertEquals(TestJobBuilder.DOCUMENTATION, createdJob.getDocumentation());
        assertEquals(TestJobBuilder.OTHER_ACTIVITIES, createdJob.getOtherActivities());
        assertEquals(TestJobBuilder.AGREEMENTS, createdJob.getAgreements());
        assertEquals(Boolean.TRUE, createdJob.getActive());
    }

    @Test
    public void givenExistingJobThenFindsItById() {
        Long createdJobId = jobService.createJob(TestJobBuilder.buildValidJob());
        Optional<Job> createdJob = jobService.findById(createdJobId);
        assertTrue(createdJob.isPresent());
    }

    @Test
    public void givenExistingJobThenRemovesIt() {
        Long createdJobId = jobService.createJob(TestJobBuilder.buildValidJob());
        Job createdJob = jobService.getById(createdJobId);
        jobService.removeJob(createdJob);
        assertEquals(Boolean.FALSE, createdJob.getActive());
    }

    @Test
    public void givenValidDataOnUpdateThenUpdatesJob() {
        Long createdJobId = jobService.createJob(TestJobBuilder.buildValidJob());
        Job createdJob = jobService.getById(createdJobId);
        Job updatedJob = TestJobBuilder.buildValidUpdatedJob(createdJob);
        jobService.updateJob(updatedJob);
        Job finalJob = jobService.getById(createdJobId);
        assertEquals(TestJobBuilder.TYPE_UPDATE, finalJob.getType());
        assertEquals(TestJobBuilder.TITLE + TestJobBuilder.UPDATE, finalJob.getTitle());
        assertEquals(TestJobBuilder.EXP_LEVEL_UPDATE, finalJob.getExperienceLevel());
        assertEquals(TestJobBuilder.EMP_TYPE_UPDATE, finalJob.getEmploymentType());
        assertEquals(TestJobBuilder.SALARY_MIN_UPDATE, finalJob.getSalaryMin());
        assertEquals(TestJobBuilder.SALARY_MAX_UPDATE, finalJob.getSalaryMax());
        assertEquals(TestJobBuilder.START_DATE + TestJobBuilder.UPDATE, finalJob.getStartDate());
        assertEquals(TestJobBuilder.CONTRACT_DURATION + TestJobBuilder.UPDATE, finalJob.getContractDuration());
        assertEquals(!TestJobBuilder.FLEXIBLE_WORK_HOURS, finalJob.getFlexibleWorkHours());
        assertEquals(TestJobBuilder.CURRENCY_UPDATE, finalJob.getCurrency());
        assertEquals(TestJobBuilder.DESCRIPTION + TestJobBuilder.UPDATE, finalJob.getDescription());
        assertEquals(TestJobBuilder.PROJECT_INDUSTRY + TestJobBuilder.UPDATE, finalJob.getProjectIndustry());
        assertEquals(TestJobBuilder.PROJECT_TEAM_SIZE_UPDATE, finalJob.getProjectTeamSize());
        assertEquals(TestJobBuilder.PROJECT_DESCRIPTION + TestJobBuilder.UPDATE, finalJob.getProjectDescription());
        assertEquals(TestJobBuilder.AGREEMENTS + TestJobBuilder.UPDATE, finalJob.getAgreements());
        assertEquals(Boolean.TRUE, finalJob.getActive());
    }

    @Test(expected = JobServiceException.class)
    public void givenInvalidRSQLSyntaxThenThrowsJobServiceException() {
        jobService.findByRSQLCondition("title=lik='test'");
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenInvalidJobTypeThenThrowsConstraintViolationException() {
        jobService.createJob(TestJobBuilder.buildJobWithInvalidType());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenInvalidExperienceLevelThenThrowsConstraintViolationException() {
        jobService.createJob(TestJobBuilder.buildJobWithInvalidExperienceLevel());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenInvalidEmploymentTypeThenThrowsConstraintViolationException() {
        jobService.createJob(TestJobBuilder.buildJobWithInvalidEmploymentType());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenNotExistingTechStacksThenThrowsConstraintViolationException() {
        jobService.createJob(TestJobBuilder.buildJobWithRelationsToNotExistingTechStacks());
    }
}
