package getjobin.it.portal.jobservice.domain.techstack.entity.validation;

import getjobin.it.portal.jobservice.domain.job.control.JobTechStackRelationRepository;
import getjobin.it.portal.jobservice.domain.job.entity.JobTechStackRelation;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class TechStackUsageValidator implements ConstraintValidator<TechStackUsageValidation, TechStack> {

    @Autowired
    private JobTechStackRelationRepository techStackRelationRepository;

    @Override
    public void initialize(TechStackUsageValidation constraintAnnotation) { }

    @Override
    public boolean isValid(TechStack techStack, ConstraintValidatorContext context) {
        boolean isValid = true;
        List<JobTechStackRelation> relationsToTechStack = techStackRelationRepository.findByTechStackId(techStack.getId());
        if(!relationsToTechStack.isEmpty()) {
            isValid = false;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MessageFormat.format("Cannot remove tech stack, it is assigned to {0} job(s). Ids: {1}",
                    String.valueOf(relationsToTechStack.size()), getCommaSeparatedIds(relationsToTechStack)))
                    .addConstraintViolation();
        }
        return isValid;
    }

    private String getCommaSeparatedIds(List<JobTechStackRelation> relationsToTechStack) {
        return relationsToTechStack.stream()
                .map(JobTechStackRelation::getJobId)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
