package getjobin.it.portal.jobservice.domain.job.entity.validation;

import getjobin.it.portal.jobservice.domain.job.control.enums.JobType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JobTypeValidator implements ConstraintValidator<JobTypeValidation, String> {

    @Override
    public void initialize(JobTypeValidation constraintAnnotation) { }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(JobType.fromString(value).isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MessageFormat.format("Allowed job types: {0}, specified {1}",
                    Arrays.stream(JobType.values())
                    .map(JobType::getLiteral)
                    .collect(Collectors.joining(", ")), value))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
