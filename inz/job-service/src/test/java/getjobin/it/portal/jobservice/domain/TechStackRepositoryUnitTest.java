package getjobin.it.portal.jobservice.domain;

import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TechStackRepositoryUnitTest {

    private static final String TEST_TECH_STACK_NAME = "obieranie ziemniaków";
    private static final String UPDATE = "update";

    private TechStack validTechStack;

    @Autowired
    private TechStackRepository techStackRepository;

    @Before
    public void buildTestTechStacks() {
        validTechStack = buildValidTechStack();
    }

    private TechStack buildValidTechStack() {
        return TechStack.builder()
                .withName(TEST_TECH_STACK_NAME)
                .build();
    }

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(techStackRepository);
    }

    @Test
    @Transactional
    public void givenValidDataThenCreatesNewTechStack() {
        Long techStackId = techStackRepository.createTechStack(validTechStack);
        techStackRepository.removeTechStackById(techStackId);
        assertNotNull(techStackId);
    }

    @Test
    @Transactional
    public void givenExistingTechStackThenFindsItById() {
        Long techStackId = techStackRepository.createTechStack(validTechStack);
        TechStack foundTechStack = techStackRepository.findById(techStackId);
        techStackRepository.removeTechStackById(techStackId);
        assertEquals(techStackId, foundTechStack.getId());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenEmptyNameOnCreateThenThrowsConstraintViolationException() {
        techStackRepository.createTechStack(TechStack.builder()
                .withName("")
                .build());
    }

    @Test
    @Transactional
    public void givenValidDataOnUpdateThenUpdatedExistingTechStack() {
        Long techStackId = techStackRepository.createTechStack(validTechStack);
        TechStack foundTechStack = techStackRepository.findById(techStackId);
        TechStack updatedTechStack = TechStack.toBuilder(foundTechStack)
                .withName(TEST_TECH_STACK_NAME + UPDATE)
                .build();
        techStackRepository.updateTechStack(updatedTechStack);
        TechStack finalTechStack = techStackRepository.findById(techStackId);
        techStackRepository.removeTechStack(finalTechStack);
        assertEquals(TEST_TECH_STACK_NAME + UPDATE, finalTechStack.getName());
    }
}
