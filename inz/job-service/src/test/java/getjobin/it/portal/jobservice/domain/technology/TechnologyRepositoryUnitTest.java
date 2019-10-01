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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TechnologyRepositoryUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private TechnologyRepository technologyRepository;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(technologyRepository);
    }

    @Test
    public void givenValidDataThenCreatesTechnology() {
        Long technologyId = technologyRepository.save(TestTechnologyBuilder.buildValidTechnology());
        assertNotNull(technologyId);
    }

    @Test
    public void givenExistingTechnologyThenFindsItById() {
        Long technologyId = technologyRepository.save(TestTechnologyBuilder.buildValidTechnology());
        Optional<Technology> foundTechnology = technologyRepository.findById(technologyId);
        assertTrue(foundTechnology.isPresent());
    }

    @Test
    public void givenExistingTechnologyThenRemovesIt() {
        Long technologyId = technologyRepository.save(TestTechnologyBuilder.buildValidTechnology());
        technologyRepository.removeById(technologyId);
        Optional<Technology> removedTechnology = technologyRepository.findById(technologyId);
        assertTrue(removedTechnology.isEmpty());
    }


    @Test
    public void givenValidDataOnUpdateThenUpdatesTechnology() {
        Long technologyId = technologyRepository.save(TestTechnologyBuilder.buildValidTechnology());
        Technology foundTechnology = technologyRepository.getById(technologyId);
        Technology updatedTechnology = TestTechnologyBuilder.buildValidUpdatedTechnology(foundTechnology);
        technologyRepository.update(updatedTechnology);
        Technology finalTechnology = technologyRepository.getById(technologyId);
        assertEquals(TestTechnologyBuilder.NAME + UPDATE, finalTechnology.getName());
        assertEquals(TestTechnologyBuilder.IMAGE_URL + UPDATE, finalTechnology.getImageUrl());
    }

}
