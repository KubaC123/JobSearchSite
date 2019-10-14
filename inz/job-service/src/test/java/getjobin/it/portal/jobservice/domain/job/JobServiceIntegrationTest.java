package getjobin.it.portal.jobservice.domain.job;

import getjobin.it.portal.jobservice.domain.IntegrationTest;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.TestJobBuilder;
import getjobin.it.portal.jobservice.infrastructure.exception.JobServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JobServiceIntegrationTest {

    private static final int NUMBER_OF_TECH_STACKS = 5;
    private static final int NUMBER_OF_LOCATIONS = 2;

    @Autowired
    private JobService jobService;

    @Autowired
    private IntegrationTest integrationTest;

    @Before
    public void init() {
        integrationTest.init();
    }

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(jobService);
        assertNotNull(integrationTest);
    }

    @Test
    public void givenValidJobThenCreatesIt() {
        Job createdJob = integrationTest.createValidJob();
        assertSimpleAttributes(createdJob);
        assertReferenceAttributes(createdJob);
    }

    public void assertSimpleAttributes(Job job) {
        assertEquals(TestJobBuilder.TYPE, job.getType());
        assertEquals(TestJobBuilder.TITLE, job.getTitle());
        assertEquals(TestJobBuilder.EXP_LEVEL, job.getExperienceLevel());
        assertEquals(TestJobBuilder.EMP_TYPE, job.getEmploymentType());
        assertEquals(TestJobBuilder.SALARY_MIN, job.getSalaryMin());
        assertEquals(TestJobBuilder.SALARY_MAX, job.getSalaryMax());
        assertEquals(TestJobBuilder.START_DATE, job.getStartDate());
        assertEquals(TestJobBuilder.CONTRACT_DURATION, job.getContractDuration());
        assertEquals(TestJobBuilder.FLEXIBLE_WORK_HOURS, job.getFlexibleWorkHours());
        assertEquals(TestJobBuilder.CURRENCY, job.getCurrency());
        assertEquals(TestJobBuilder.DESCRIPTION, job.getDescription());
        assertEquals(TestJobBuilder.PROJECT_INDUSTRY, job.getProjectIndustry());
        assertEquals(TestJobBuilder.PROJECT_TEAM_SIZE, job.getProjectTeamSize());
        assertEquals(TestJobBuilder.PROJECT_DESCRIPTION, job.getProjectDescription());
        assertEquals(TestJobBuilder.DEVELOPMENT, job.getDevelopment());
        assertEquals(TestJobBuilder.MAINTENANCE, job.getMaintenance());
        assertEquals(TestJobBuilder.TESTING, job.getTesting());
        assertEquals(TestJobBuilder.CLIENT_SUPPORT, job.getClientSupport());
        assertEquals(TestJobBuilder.DOCUMENTATION, job.getDocumentation());
        assertEquals(TestJobBuilder.OTHER_ACTIVITIES, job.getOtherActivities());
        assertEquals(TestJobBuilder.AGREEMENTS, job.getAgreements());
        assertEquals(Boolean.TRUE, job.getActive());
    }

    public void assertReferenceAttributes(Job job) {
        assertEquals(integrationTest.getCompany(), job.getCompany());
        assertEquals(integrationTest.getCategory(), job.getCategory());
        assertEquals(integrationTest.getTechnology(), job.getTechnology());
        assertTrue(job.getTechStackRelations().isPresent());
        assertEquals(NUMBER_OF_TECH_STACKS, job.getTechStackRelations().get().size());
        job.getTechStackRelations().get()
                .forEach(techStackRelation -> {
                    assertEquals(job.getId(), techStackRelation.getJobId());
                    assertEquals(TestJobBuilder.TECH_STACK_RELATION_EXP_LEVEL, techStackRelation.getExperienceLevel());
                });
        assertTrue(job.getLocationRelations().isPresent());
        assertEquals(NUMBER_OF_LOCATIONS, job.getLocationRelations().get().size());
        job.getLocationRelations().get()
                .forEach(locationRelation -> {
                    assertEquals(job.getId(), locationRelation.getJobId());
                    assertEquals(TestJobBuilder.LOCATION_RELATION_REMOTE, locationRelation.getRemote());
                });
    }

    @Test
    public void givenValidDataOnUpdateThenUpdatesJob() {
        Job createdJob = integrationTest.createValidJob();
        Job updatedJob = TestJobBuilder.buildValidUpdatedJob(createdJob);
        Long jobId = jobService.update(updatedJob);
        Job finalJob = jobService.getById(jobId);
        assertSimpleAttributesAfterUpdate(finalJob);
        assertReferenceAttributes(finalJob);
    }

    private void assertSimpleAttributesAfterUpdate(Job finalJob) {
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

    @Test(expected = ConstraintViolationException.class)
    public void givenJobWithoutCompanyThenThrowsConstraintViolationException() {
        Job jobToCreate = Job.toBuilder(integrationTest.buildValidJob())
                .company(null)
                .build();
        jobService.create(jobToCreate);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenJobWithoutCategoryThenThrowsConstraintViolationException() {
        Job jobToCreate = Job.toBuilder(integrationTest.buildValidJob())
                .category(null)
                .build();
        jobService.create(jobToCreate);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenJobWithoutTechnologyThenThrowsConstraintViolationException() {
        Job jobToCreate = Job.toBuilder(integrationTest.buildValidJob())
                .technology(null)
                .build();
        jobService.create(jobToCreate);
    }

    @Test(expected = JobServiceException.class)
    public void givenInvalidRSQLSyntaxThenThrowsJobServiceException() {
        jobService.findByRsqlCondition("title=lik='test'");
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenInvalidJobTypeThenThrowsConstraintViolationException() {
        Job jobToCreate = Job.toBuilder(integrationTest.buildValidJob())
                .type("random")
                .build();
        jobService.create(jobToCreate);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenInvalidExperienceLevelThenThrowsConstraintViolationException() {
        Job jobToCreate = Job.toBuilder(integrationTest.buildValidJob())
                .experienceLevel("random")
                .build();
        jobService.create(jobToCreate);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenInvalidEmploymentTypeThenThrowsConstraintViolationException() {
        Job jobToCreate = Job.toBuilder(integrationTest.buildValidJob())
                .employmentType("random")
                .build();
        jobService.create(jobToCreate);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenNotExistingTechStacksThenThrowsConstraintViolationException() {
        Job jobToCreate = Job.toBuilder(integrationTest.buildValidJob())
                .techStackRelations(TestJobBuilder.getNotExistingTechStacks())
                .build();
        jobService.create(jobToCreate);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenNotExistingLocationsThenThrowsConstraintViolationException() {
        Job jobToCreate = Job.toBuilder(integrationTest.buildValidJob())
                .locationRelations(TestJobBuilder.getNotExistingLocations())
                .build();
        jobService.create(jobToCreate);
    }
}
