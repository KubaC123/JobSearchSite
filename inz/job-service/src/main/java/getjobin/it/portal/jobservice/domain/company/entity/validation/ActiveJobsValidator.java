package getjobin.it.portal.jobservice.domain.company.entity.validation;

import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class ActiveJobsValidator implements ConstraintValidator<ActiveJobsValidation, Company> {

    @Autowired
    private JobService jobService;

    @Override
    public void initialize(ActiveJobsValidation constraintAnnotation) { }

    @Override
    public boolean isValid(Company company, ConstraintValidatorContext context) {
        boolean isValid = true;
        List<Job> companyJobs = jobService.findByCompany(company);
        if(!companyJobs.isEmpty()) {
            isValid = false;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MessageFormat.format("Cannot remove company. It has {0} active jobs offers. Ids: {1}",
                    String.valueOf(companyJobs.size()), getCommaSeparatedJobIds(companyJobs)))
                    .addConstraintViolation();
        }
        return isValid;
    }

    private String getCommaSeparatedJobIds(List<Job> jobs) {
        return jobs.stream()
                .map(Job::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
