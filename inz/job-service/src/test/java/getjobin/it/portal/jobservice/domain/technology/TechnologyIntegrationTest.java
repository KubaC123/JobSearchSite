package getjobin.it.portal.jobservice.domain.technology;

import getjobin.it.portal.jobservice.domain.IntegrationTest;
import getjobin.it.portal.jobservice.domain.technology.control.TechnologyService;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.technology.entity.TestTechnologyBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TechnologyIntegrationTest {

    @Autowired
    private TechnologyService technologyService;

    @Autowired
    private IntegrationTest integrationTest;

    @Before
    public void init() {
        integrationTest.init();
    }

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(technologyService);
        assertNotNull(integrationTest);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenTechnologyWithActiveJobsOnRemoveThenThrowsConstraintViolationException() {
        Long technologyId = technologyService.create(TestTechnologyBuilder.buildValidTechnology());
        Technology createdTechnology = technologyService.getById(technologyId);
        integrationTest.createValidJobWith(createdTechnology);
        technologyService.remove(createdTechnology);
    }
}
