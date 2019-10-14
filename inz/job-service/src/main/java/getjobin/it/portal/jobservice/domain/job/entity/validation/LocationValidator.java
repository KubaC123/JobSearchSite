package getjobin.it.portal.jobservice.domain.job.entity.validation;

import getjobin.it.portal.jobservice.domain.job.entity.JobLocationRelation;
import getjobin.it.portal.jobservice.domain.location.control.LocationService;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocationValidator implements ConstraintValidator<LocationValidation, List<JobLocationRelation>> {

    @Autowired
    private LocationService locationService;

    @Override
    public void initialize(LocationValidation constraintAnnotation) { }

    @Override
    public boolean isValid(List<JobLocationRelation> locationRelations, ConstraintValidatorContext context) {
        return validateLocationRelations(locationRelations, context);
    }

    private boolean validateLocationRelations(List<JobLocationRelation> locationRelations, ConstraintValidatorContext context) {
        if(locationRelations == null || locationRelations.isEmpty()) {
            addMessageToContext(context,"At least one location must be provided");
            return false;
        }
        List<Long> specifiedLocationIds = getLocationIds(locationRelations);
        Set<Long> duplicatedLocationIds = findDuplicatedLocationIds(specifiedLocationIds);
        if(!duplicatedLocationIds.isEmpty()) {
            addMessageToContext(context,
                    MessageFormat.format("Duplicated location id(s): {0}.", getCommaSeparatedIds(duplicatedLocationIds.stream())));
            return false;
        }
        List<Long> notExistingLocationIds = findNotExistingLocationIds(specifiedLocationIds);
        if(!notExistingLocationIds.isEmpty()) {
            addMessageToContext(context,
                    MessageFormat.format("Location does not exists, id(s): {0}.", getCommaSeparatedIds(notExistingLocationIds.stream())));
            return false;
        }
        return true;
    }

    private void addMessageToContext(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

    private String getCommaSeparatedIds(Stream<Long> stream) {
        return stream
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    private List<Long> getLocationIds(List<JobLocationRelation> locationRelations) {
        return locationRelations.stream()
                .map(JobLocationRelation::getLocationId)
                .collect(Collectors.toList());
    }

    private Set<Long> findDuplicatedLocationIds(List<Long> techStackIds) {
        return techStackIds.stream()
                .filter(id -> Collections.frequency(techStackIds, id) > 1)
                .collect(Collectors.toSet());
    }

    private List<Long> findNotExistingLocationIds(List<Long> techStackIds) {
        List<Long> foundTechStackIds = findLocations(techStackIds);
        return techStackIds.stream()
                .filter(techStackId -> !foundTechStackIds.contains(techStackId))
                .collect(Collectors.toList());
    }

    private List<Long> findLocations(List<Long> techStackIds) {
        return locationService.findByIds(techStackIds).stream()
                .map(Location::getId)
                .collect(Collectors.toList());
    }
}
