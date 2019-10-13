package getjobin.it.portal.jobservice.domain.location.entity.validation;

import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class LocationUsageValidator implements ConstraintValidator<LocationUsageValidation, Location> {

    @Autowired
    private JobService jobService;

    @Override
    public void initialize(LocationUsageValidation constraintAnnotation) { }

    @Override
    public boolean isValid(Location location, ConstraintValidatorContext context) {
        boolean isValid = true;
        List<Job> jobsInLocation = jobService.findByRsqlCondition("locationRelations.jobId==" + location.getId());
        if(!jobsInLocation.isEmpty()) {
            isValid = false;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MessageFormat.format("Cannot remove location, there are {0} jobs in it. Ids: {1}",
                    String.valueOf(jobsInLocation.size()), getCommaSeparatedIds(jobsInLocation)))
                    .addConstraintViolation();
        }
        return isValid;
    }

    private String getCommaSeparatedIds(List<Job> jobsInLocation) {
        return jobsInLocation.stream()
                .map(Job::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
