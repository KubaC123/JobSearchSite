package getjobin.it.portal.jobservice.domain.location;

import getjobin.it.portal.jobservice.domain.IntegrationTest;
import getjobin.it.portal.jobservice.domain.location.control.LocationService;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.domain.location.entity.TestLocationBuilder;
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
public class LocationIntegrationTest {

    @Autowired
    private LocationService locationService;

    @Autowired
    private IntegrationTest integrationTest;

    @Before
    public void init() {
        integrationTest.init();
    }

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(locationService);
        assertNotNull(integrationTest);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenJobsInLocationOnRemoveThenThrowsConstraintViolationException() {
        Long locationId = locationService.create(TestLocationBuilder.buildValidLocation());
        Location createdLocation = locationService.getById(locationId);
        integrationTest.createValidJobWithLocations(Collections.singletonList(createdLocation));
        locationService.remove(createdLocation);
    }
}