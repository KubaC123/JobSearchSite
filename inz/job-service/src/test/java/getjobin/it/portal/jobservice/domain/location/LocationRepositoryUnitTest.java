package getjobin.it.portal.jobservice.domain.location;

import getjobin.it.portal.jobservice.domain.location.control.LocationRepository;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.domain.location.entity.TestLocationBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class LocationRepositoryUnitTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(locationRepository);
    }

    @Test
    public void givenValidDataThenCreatesLocation() {
        Long locationId = locationRepository.save(TestLocationBuilder.buildValidLocation());
        assertNotNull(locationId);
    }

    @Test
    public void givenExistingLocationThenFindsItById() {
        Long locationId = locationRepository.save(TestLocationBuilder.buildValidLocation());
        Optional<Location> foundLocation = locationRepository.findById(locationId);
        assertTrue(foundLocation.isPresent());
    }

    @Test
    public void givenExistingLocationThenRemovesIt() {
        Long locationId = locationRepository.save(TestLocationBuilder.buildValidLocation());
        locationRepository.removeById(locationId);
        Optional<Location> removedLocation = locationRepository.findById(locationId);
        assertTrue(removedLocation.isEmpty());
    }
}
