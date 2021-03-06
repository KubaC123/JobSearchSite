package getjobin.it.portal.jobservice.domain.location.control;

import getjobin.it.portal.jobservice.domain.location.entity.Location;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class LocationService {

    @Autowired
    private Validator validator;

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public List<Location> findByIds(List<Long> locationIds) {
        return locationRepository.findByIds(locationIds);
    }

    public Optional<Location> findById(Long locationId) {
        return locationRepository.findById(locationId);
    }

    public Location getById(Long locationId) {
        return locationRepository.getById(locationId);
    }

    public Long create(Location location) {
        validate(location);
        return locationRepository.save(location);
    }

    private void validate(Location location) {
        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Long update(Location location) {
        validate(location);
        return locationRepository.update(location);
    }

    public void remove(Location location) {
        validateOnRemove(location);
        locationRepository.remove(location);
    }

    private void validateOnRemove(Location category) {
        Set<ConstraintViolation<Location>> violations = validator.validate(category, Location.DeleteValidations.class);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public boolean isSearchTextLocationRelated(String searchText) {
        return findAll().stream()
                .anyMatch(location -> doesSearchTextOccur(location, searchText));
    }

    public boolean doesSearchTextOccur(Location location, String searchText) {
        return Stream.of(
                location.getCity(),
                location.getStreet(),
                location.getCountryName())
                .anyMatch(loc -> loc.equals(searchText) || loc.equals(StringUtils.stripAccents(searchText)));
    }
}
