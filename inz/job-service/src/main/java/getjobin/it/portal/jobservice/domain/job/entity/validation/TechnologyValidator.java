package getjobin.it.portal.jobservice.domain.job.entity.validation;

import getjobin.it.portal.jobservice.domain.technology.control.TechnologyService;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.Optional;

public class TechnologyValidator implements ConstraintValidator<TechnologyValidation, Technology> {

    @Autowired
    private TechnologyService technologyService;

    @Override
    public void initialize(TechnologyValidation constraintAnnotation) { }

    @Override
    public boolean isValid(Technology technology, ConstraintValidatorContext context) {
        return Optional.ofNullable(technology)
                .map(specifiedCategory -> validateExistence(technology, context))
                .orElse(false);
    }

    private boolean validateExistence(Technology technology, ConstraintValidatorContext context) {
        return technologyService.findById(technology.getId())
                .map(foundTechnology -> true)
                .orElseGet(() -> {
                    addMessageToContext(MessageFormat.format("Technology with id {0} does not exist", technology.getId()), context);
                    return false;
                });
    }

    private void addMessageToContext(String message, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message);
    }
}
