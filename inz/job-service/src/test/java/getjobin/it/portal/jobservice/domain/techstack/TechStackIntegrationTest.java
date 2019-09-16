package getjobin.it.portal.jobservice.domain.techstack;

import getjobin.it.portal.jobservice.domain.job.entity.TestJobBuilder;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.domain.techstack.entity.TestTechStackBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TechStackIntegrationTest {

    @Autowired
    private TechStackService techStackService;

    @Autowired
    private JobService jobService;

    @Test(expected = ConstraintViolationException.class)
    public void givenTechStackAssignedToJobOnRemoveThenThrowsConstraintViolationException() {
        Long techStackId = techStackService.createTechStack(TestTechStackBuilder.buildValidTechStack());
        TechStack createdTechStack = techStackService.getById(techStackId);
        jobService.createJob(TestJobBuilder.buildValidJobWithTechStack(createdTechStack));
        techStackService.removeTechStack(createdTechStack);
    }
}
