package getjobin.it.portal.jobservice.domain.technology;

import getjobin.it.portal.jobservice.domain.technology.control.TechnologyService;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.technology.entity.TestTechnologyBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TechnologyServiceUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private TechnologyService technologyService;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(technologyService);
    }

    @Test
    @Transactional
    public void givenValidDataThenCreatesNewTechnology() {
        Long createdTechnologyId = technologyService.createTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology createdTechnology = technologyService.getById(createdTechnologyId);
        technologyService.removeTechnology(createdTechnology);
        assertEquals(createdTechnologyId, createdTechnology.getId());
    }

    @Test
    @Transactional
    public void givenExistingTechStackThenFindsItById() {
        Long technologyId = technologyService.createTechnology(TestTechnologyBuilder.buildValidTechnology());
        Optional<Technology> foundTechnology = technologyService.findById(technologyId);
        technologyService.removeTechnology(foundTechnology.orElseThrow());
        assertTrue(foundTechnology.isPresent());
    }

    @Test
    @Transactional
    public void givenValidDataOnUpdateThenUpdatedExistingTechStack() {
        Long technologyId = technologyService.createTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology foundTechnology = technologyService.getById(technologyId);
        Technology updatedTechnology = TestTechnologyBuilder.buildValidUpdatedTechnology(foundTechnology);
        technologyService.updateTechnology(updatedTechnology);
        Technology finalTechnology = technologyService.getById(technologyId);
        technologyService.removeTechnology(finalTechnology);
        assertEquals(TestTechnologyBuilder.TEST_TECHNOLOGY_NAME + UPDATE, finalTechnology.getName());
        assertEquals(TestTechnologyBuilder.TEST_TECHNOLOGY_IMAGE_URL + UPDATE, finalTechnology.getImageUrl());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenEmptyNameOnCreateThenThrowsConstraintViolationException() {
        technologyService.createTechnology(TestTechnologyBuilder.buildTechnologyWithEmptyName());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenNullNameOnCreateThenThrowsConstraintViolationException() {
        technologyService.createTechnology(TestTechnologyBuilder.buildTechnologyWithNullName());
    }
}
