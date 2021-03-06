package getjobin.it.portal.jobservice.domain.job.entity.validation;

import getjobin.it.portal.jobservice.domain.category.control.CategoryService;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.Optional;

public class CategoryValidator implements ConstraintValidator<CategoryValidation, Category> {

    @Autowired
    private CategoryService categoryService;

    @Override
    public void initialize(CategoryValidation constraintAnnotation) { }

    @Override
    public boolean isValid(Category category, ConstraintValidatorContext context) {
        return Optional.ofNullable(category)
                .map(specifiedCategory -> validateExistence(specifiedCategory, context))
                .orElse(false);
    }

    public boolean validateExistence(Category category, ConstraintValidatorContext context){
        return categoryService.findById(category.getId())
                .map(foundTechnology -> true)
                .orElseGet(() -> {
                    addMessageToContext(MessageFormat.format("Category with id {0} does not exist", category.getId()), context);
                    return false;
                });
    }

    private void addMessageToContext(String message, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message);
    }
}
