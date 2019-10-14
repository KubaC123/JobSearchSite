package getjobin.it.portal.jobservice.domain.techstack;

import getjobin.it.portal.jobservice.domain.IntegrationTest;
import getjobin.it.portal.jobservice.domain.job.entity.TestJobBuilder;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.domain.techstack.entity.TestTechStackBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TechStackIntegrationTest {

    @Autowired
    private TechStackService techStackService;

    @Autowired
    private IntegrationTest integrationTest;

    @Before
    public void init() {
        integrationTest.init();
    }

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(techStackService);
        assertNotNull(integrationTest);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenTechStackAssignedToJobOnRemoveThenThrowsConstraintViolationException() {
        Long techStackId = techStackService.create(TestTechStackBuilder.buildValidTechStack());
        TechStack createdTechStack = techStackService.getById(techStackId);
        integrationTest.createValidJobWithTechStacks(Collections.singletonList(createdTechStack));
        techStackService.remove(createdTechStack);
    }
}
