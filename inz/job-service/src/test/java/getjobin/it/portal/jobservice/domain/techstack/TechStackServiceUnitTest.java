package getjobin.it.portal.jobservice.domain.techstack;

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
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TechStackServiceUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private TechStackService techStackService;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(techStackService);
    }

    @Test
    @Transactional
    public void givenValidDataThenCreatesNewTechStacks() {
        Long createdTechStackId = techStackService.createTechStack(TestTechStackBuilder.buildValidTechStack());
        TechStack createdTechStack = techStackService.getById(createdTechStackId);
        techStackService.removeTechStack(createdTechStack);
        assertEquals(createdTechStackId, createdTechStack.getId());
    }

    @Test
    @Transactional
    public void givenExistingTechStackThenFindsItById() {
        Long techStackId = techStackService.createTechStack(TestTechStackBuilder.buildValidTechStack());
        Optional<TechStack> foundTechStack = techStackService.findById(techStackId);
        techStackService.removeTechStack(foundTechStack.orElseThrow());
        assertTrue(foundTechStack.isPresent());
    }

    @Test
    @Transactional
    public void givenValidDataOnUpdateThenUpdatedExistingTechStack() {
        Long techStackId = techStackService.createTechStack(TestTechStackBuilder.buildValidTechStack());
        TechStack foundTechStack = techStackService.getById(techStackId);
        TechStack updatedTechStack = TestTechStackBuilder.buildValidUpdatedTechStack(foundTechStack);
        techStackService.updateTechStack(updatedTechStack);
        TechStack finalTechStack = techStackService.getById(techStackId);
        techStackService.removeTechStack(finalTechStack);
        assertEquals(TestTechStackBuilder.TEST_TECH_STACK_NAME + UPDATE, finalTechStack.getName());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenEmptyNameOnCreateThenThrowsConstraintViolationException() {
        techStackService.createTechStack(TestTechStackBuilder.buildTechStackWithEmptyName());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenNullNameOnCreateThenThrowsConstraintViolationException() {
        techStackService.createTechStack(TestTechStackBuilder.buildTechStackWithNullName());
    }
}
