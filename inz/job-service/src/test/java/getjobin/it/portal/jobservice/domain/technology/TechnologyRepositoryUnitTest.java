package getjobin.it.portal.jobservice.domain.technology;

import getjobin.it.portal.jobservice.domain.technology.control.TechnologyRepository;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.technology.entity.TestTechnologyBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TechnologyRepositoryUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private TechnologyRepository technologyRepository;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(technologyRepository);
    }

    @Test
    @Transactional
    public void givenValidDataThenCreatesNewTechnology() {
        Long technologyId = technologyRepository.saveTechnology(TestTechnologyBuilder.buildValidTechnology());
        technologyRepository.removeTechnologyById(technologyId);
        assertNotNull(technologyId);
    }

    @Test
    @Transactional
    public void givenExistingTechnologyThenFindsItById() {
        Long technologyId = technologyRepository.saveTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology foundTechnology = technologyRepository.getById(technologyId);
        technologyRepository.removeTechnology(foundTechnology);
        assertEquals(technologyId, foundTechnology.getId());
    }

    @Test
    @Transactional
    public void givenExistingTechnologyThenRemovesIt() {
        Long technologyId = technologyRepository.saveTechnology(TestTechnologyBuilder.buildValidTechnology());
        technologyRepository.removeTechnologyById(technologyId);
        Optional<Technology> removedTechnology = technologyRepository.findById(technologyId);
        assertTrue(removedTechnology.isEmpty());
    }


    @Test
    @Transactional
    public void givenValidDataOnUpdateThenUpdatedExistingTechnology() {
        Long technologyId = technologyRepository.saveTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology foundTechnology = technologyRepository.getById(technologyId);
        Technology updatedTechnology = TestTechnologyBuilder.buildValidUpdatedTechnology(foundTechnology);
        technologyRepository.updateTechnology(updatedTechnology);
        Technology finalTechnology = technologyRepository.getById(technologyId);
        technologyRepository.removeTechnology(finalTechnology);
        assertEquals(TestTechnologyBuilder.TEST_TECHNOLOGY_NAME + UPDATE, finalTechnology.getName());
        assertEquals(TestTechnologyBuilder.TEST_TECHNOLOGY_IMAGE_URL + UPDATE, finalTechnology.getImageUrl());
    }

}
