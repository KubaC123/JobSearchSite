package getjobin.it.portal.jobservice.domain.category.entity.validation;

import getjobin.it.portal.jobservice.domain.category.entity.Category;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryUsageValidator implements ConstraintValidator<CategoryUsageValidation, Category> {

    @Autowired
    private JobService jobService;

    @Override
    public void initialize(CategoryUsageValidation constraintAnnotation) { }

    @Override
    public boolean isValid(Category category, ConstraintValidatorContext context) {
        boolean isValid = true;
        List<Job> categoryJobs = jobService.findByRSQLCondition("category.id==" + category.getId());
        if(!categoryJobs.isEmpty()) {
            isValid = false;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MessageFormat.format("Cannot remove category, there are {0} jobs using it. Ids: {1}",
                    String.valueOf(categoryJobs.size()), getCommaSeparatedIds(categoryJobs)))
                    .addConstraintViolation();
        }
        return isValid;
    }

    private String getCommaSeparatedIds(List<Job> categoryJobs) {
        return categoryJobs.stream()
                .map(Job::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
