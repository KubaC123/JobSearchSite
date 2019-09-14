package getjobin.it.portal.jobservice.domain.job.entity.validation;

import getjobin.it.portal.jobservice.domain.job.control.enums.EmploymentType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EmploymentTypeValidator implements ConstraintValidator<EmploymentTypeValidation, String> {

    @Override
    public void initialize(EmploymentTypeValidation constraintAnnotation) { }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(EmploymentType.fromString(value).isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MessageFormat.format("Allowed employment types: {0}, specified {1}", Arrays.stream(EmploymentType.values())
                    .map(EmploymentType::getLiteral)
                    .collect(Collectors.joining(", ")), value))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
