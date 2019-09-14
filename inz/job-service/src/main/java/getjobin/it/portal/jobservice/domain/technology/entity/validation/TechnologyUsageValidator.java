package getjobin.it.portal.jobservice.domain.technology.entity.validation;

import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class TechnologyUsageValidator implements ConstraintValidator<TechnologyUsageValidation, Technology> {

    @Autowired
    private JobService jobService;

    @Override
    public void initialize(TechnologyUsageValidation constraintAnnotation) { }

    @Override
    public boolean isValid(Technology technology, ConstraintValidatorContext context) {
        boolean isValid = true;
        List<Job> technologyJobs = jobService.findByTechnology(technology);
        if(!technologyJobs.isEmpty()) {
            isValid = false;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MessageFormat.format("Cannot remove technology, there are {0} active jobs using it. Ids: {1}",
                    String.valueOf(technologyJobs.size()), getCommaSeparatedIds(technologyJobs)))
                    .addConstraintViolation();
        }
        return isValid;
    }

    private String getCommaSeparatedIds(List<Job> technologyJobs) {
        return technologyJobs.stream()
                .map(Job::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
