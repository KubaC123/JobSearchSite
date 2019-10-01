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
@Transactional
public class TechStackRepositoryUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private TechStackRepository techStackRepository;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(techStackRepository);
    }

    @Test
    public void givenValidDataThenCreatesTechStack() {
        Long techStackId = techStackRepository.save(TestTechStackBuilder.buildValidTechStack());
        assertNotNull(techStackId);
    }

    @Test
    public void givenExistingTechStackThenFindsItById() {
        Long techStackId = techStackRepository.save(TestTechStackBuilder.buildValidTechStack());
        TechStack foundTechStack = techStackRepository.getById(techStackId);
        assertEquals(techStackId, foundTechStack.getId());
    }

    @Test
    public void givenExistingTechStackThenRemovesIt() {
        Long techStackId = techStackRepository.save(TestTechStackBuilder.buildValidTechStack());
        techStackRepository.removeById(techStackId);
        Optional<TechStack> removedTechStack = techStackRepository.findById(techStackId);
        assertTrue(removedTechStack.isEmpty());
    }


    @Test
    public void givenValidDataOnUpdateThenUpdatesTechStack() {
        Long techStackId = techStackRepository.save(TestTechStackBuilder.buildValidTechStack());
        TechStack foundTechStack = techStackRepository.getById(techStackId);
        TechStack updatedTechStack = TestTechStackBuilder.buildValidUpdatedTechStack(foundTechStack);
        techStackRepository.update(updatedTechStack);
        TechStack finalTechStack = techStackRepository.getById(techStackId);
        assertEquals(TestTechStackBuilder.NAME + UPDATE, finalTechStack.getName());
    }

}
