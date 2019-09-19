package getjobin.it.portal.jobservice.domain.job.entity.validation;

import getjobin.it.portal.jobservice.domain.job.control.enums.ExperienceLevel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ExperienceLevelValidator implements ConstraintValidator<ExperienceLevelValidation, String> {

    @Override
    public void initialize(ExperienceLevelValidation constraintAnnotation) { }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(ExperienceLevel.fromString(value).isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MessageFormat.format("Experience level must be provided. Allowed exp levels: {0}, specified {1}", Arrays.stream(ExperienceLevel.values())
                    .map(ExperienceLevel::getLiteral)
                    .collect(Collectors.joining(", ")), value))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
