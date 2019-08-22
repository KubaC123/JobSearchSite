package getjobin.it.portal.jobservice.domain.techstack;

import getjobin.it.portal.jobservice.domain.techstack.control.TechStackRepository;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.domain.techstack.entity.TestTechStackBuilder;
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
public class TechStackRepositoryUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private TechStackRepository techStackRepository;
    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(techStackRepository);
    }

    @Test
    @Transactional
    public void givenValidDataThenCreatesNewTechStack() {
        Long techStackId = techStackRepository.saveTechStack(TestTechStackBuilder.buildValidTechStack());
        techStackRepository.removeTechStackById(techStackId);
        assertNotNull(techStackId);
    }

    @Test
    @Transactional
    public void givenExistingTechStackThenFindsItById() {
        Long techStackId = techStackRepository.saveTechStack(TestTechStackBuilder.buildValidTechStack());
        TechStack foundTechStack = techStackRepository.getById(techStackId);
        techStackRepository.removeTechStack(foundTechStack);
        assertEquals(techStackId, foundTechStack.getId());
    }

    @Test
    @Transactional
    public void givenExistingTechStackThenRemovesIt() {
        Long techStackId = techStackRepository.saveTechStack(TestTechStackBuilder.buildValidTechStack());
        techStackRepository.removeTechStackById(techStackId);
        Optional<TechStack> removedTechStack = techStackRepository.findById(techStackId);
        assertTrue(removedTechStack.isEmpty());
    }


    @Test
    @Transactional
    public void givenValidDataOnUpdateThenUpdatedExistingTechStack() {
        Long techStackId = techStackRepository.saveTechStack(TestTechStackBuilder.buildValidTechStack());
        TechStack foundTechStack = techStackRepository.getById(techStackId);
        TechStack updatedTechStack = TestTechStackBuilder.buildValidUpdatedTechStack(foundTechStack);
        techStackRepository.updateTechStack(updatedTechStack);
        TechStack finalTechStack = techStackRepository.getById(techStackId);
        techStackRepository.removeTechStack(finalTechStack);
        assertEquals(TestTechStackBuilder.TEST_TECH_STACK_NAME + UPDATE, finalTechStack.getName());
    }

}
