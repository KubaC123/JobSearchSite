package getjobin.it.portal.jobservice.domain.location;

import getjobin.it.portal.jobservice.domain.location.control.LocationService;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.domain.location.entity.TestLocationBuilder;
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
@Transactional
public class LocationServiceUnitTest {

    @Autowired
    private LocationService locationService;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(locationService);
    }

    @Test
    public void givenValidDataThenCreatesLocation() {
        Long createdLocationId = locationService.create(TestLocationBuilder.buildValidLocation());
        Location createdLocation = locationService.getById(createdLocationId);
        assertEquals(createdLocationId, createdLocation.getId());
    }

    @Test
    public void givenExistingLocationThenFindsItById() {
        Long categoryId = locationService.create(TestLocationBuilder.buildValidLocation());
        Optional<Location> foundLocation = locationService.findById(categoryId);
        assertTrue(foundLocation.isPresent());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenNullStreetThenThrowsConstraintViolationException() {
        locationService.create(TestLocationBuilder.buildLocationWithNullStreet());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenNullCityThenThrowsConstraintViolationException() {
        locationService.create(TestLocationBuilder.buildLocationWithNullCity());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenNullCoordinatesThenThrowsConstraintViolationException() {
        locationService.create(TestLocationBuilder.buildLocationWithNullCoordinates());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenEmptyStreetThenThrowsConstraintViolationException() {
        locationService.create(TestLocationBuilder.buildLocationWithEmptyStreet());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenEmptyCityThenThrowsConstraintViolationException() {
        locationService.create(TestLocationBuilder.buildLocationWithEmptyCity());
    }
}
