package getjobin.it.portal.jobservice.domain.category.entity.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CategoryUsageValidator.class)
@Target( {ElementType.TYPE, ElementType.ANNOTATION_TYPE} )
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoryUsageValidation {

    String message() default "";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
