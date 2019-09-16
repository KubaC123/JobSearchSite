package getjobin.it.portal.jobservice.domain.job;

import getjobin.it.portal.jobservice.domain.job.control.JobRepository;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.TestJobBuilder;
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
public class JobRepositoryUnitTest {

    @Autowired
    private JobRepository jobRepository;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(jobRepository);
    }

    @Test
    public void givenValidDataThenCreatesJob() {
        Long createdJobId = jobRepository.saveJob(TestJobBuilder.buildValidJob());
        Job createdJob = jobRepository.getById(createdJobId);
        assertNotNull(createdJobId);
        assertEquals(TestJobBuilder.TYPE, createdJob.getType());
        assertEquals(TestJobBuilder.TITLE, createdJob.getTitle());
        assertEquals(TestJobBuilder.EXP_LEVEL, createdJob.getExperienceLevel());
        assertEquals(TestJobBuilder.EMP_TYPE, createdJob.getEmploymentType());
        assertEquals(TestJobBuilder.SALARY_MIN, createdJob.getSalaryMin());
        assertEquals(TestJobBuilder.SALARY_MAX, createdJob.getSalaryMax());
        assertEquals(TestJobBuilder.CURRENCY, createdJob.getCurrency());
        assertEquals(TestJobBuilder.DESCRIPTION, createdJob.getDescription());
        assertEquals(TestJobBuilder.AGREEMENTS, createdJob.getAgreements());
        assertEquals(TestJobBuilder.REMOTE, createdJob.getRemote());
        assertEquals(Boolean.TRUE, createdJob.getActive());
    }

    @Test
    public void givenExistingJobThenFindsItById() {
        Long createdJobId = jobRepository.saveJob(TestJobBuilder.buildValidJob());
        Optional<Job> createdJob = jobRepository.findById(createdJobId);
        assertTrue(createdJob.isPresent());
    }

    @Test
    public void givenExistingJobThenRemovesIt() {
        Long createdJobId = jobRepository.saveJob(TestJobBuilder.buildValidJob());
        Job createdJob = jobRepository.getById(createdJobId);
        jobRepository.removeJob(createdJob);
        assertEquals(Boolean.FALSE, createdJob.getActive());
    }

    @Test
    public void givenValidDataOnUpdateThenUpdatesJob() {
        Long createdJobId = jobRepository.saveJob(TestJobBuilder.buildValidJob());
        Job createdJob = jobRepository.getById(createdJobId);
        Job updatedJob = TestJobBuilder.buildValidUpdatedJob(createdJob);
        jobRepository.updateJob(updatedJob);
        Job finalJob = jobRepository.getById(createdJobId);
        assertEquals(TestJobBuilder.TYPE_UPDATE, finalJob.getType());
        assertEquals(TestJobBuilder.TITLE + TestJobBuilder.UPDATE, finalJob.getTitle());
        assertEquals(TestJobBuilder.EXP_LEVEL_UPDATE, finalJob.getExperienceLevel());
        assertEquals(TestJobBuilder.EMP_TYPE_UPDATE, finalJob.getEmploymentType());
        assertEquals(TestJobBuilder.SALARY_MIN_UPDATE, finalJob.getSalaryMin());
        assertEquals(TestJobBuilder.SALARY_MAX_UPDATE, finalJob.getSalaryMax());
        assertEquals(TestJobBuilder.CURRENCY_UPDATE, finalJob.getCurrency());
        assertEquals(TestJobBuilder.DESCRIPTION + TestJobBuilder.UPDATE, finalJob.getDescription());
        assertEquals(TestJobBuilder.AGREEMENTS + TestJobBuilder.UPDATE, finalJob.getAgreements());
        assertEquals(!TestJobBuilder.REMOTE, finalJob.getRemote());
        assertEquals(Boolean.TRUE, finalJob.getActive());
    }
}
