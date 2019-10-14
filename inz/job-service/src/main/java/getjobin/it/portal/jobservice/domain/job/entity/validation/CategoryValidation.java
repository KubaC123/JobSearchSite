package getjobin.it.portal.jobservice.domain.job.entity.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CategoryValidator.class)
@Target( {ElementType.FIELD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoryValidation {

    String message() default "Category must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
