package getjobin.it.portal.jobservice.domain.job.entity.validation;

import getjobin.it.portal.jobservice.domain.company.control.CompanyService;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.Optional;

public class CompanyValidator implements ConstraintValidator<CompanyValidation, Company> {

    @Autowired
    private CompanyService companyService;

    @Override
    public void initialize(CompanyValidation constraintAnnotation) { }

    @Override
    public boolean isValid(Company company, ConstraintValidatorContext context) {
        return Optional.ofNullable(company)
                .map(specifiedCategory -> validate(company, context))
                .orElse(false);
    }

    private boolean validate(Company company, ConstraintValidatorContext context) {
        return companyService.findById(company.getId())
                .map(foundTechnology -> true)
                .orElseGet(() -> {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                            MessageFormat.format("Company with id {0} does not exist", company.getId()));
                    return false;
                });
    }
}
