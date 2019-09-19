package getjobin.it.portal.jobservice.domain.job.entity.validation;

import getjobin.it.portal.jobservice.domain.job.entity.JobTechStackRelation;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TechStacksValidator implements ConstraintValidator<TechStacksValidation, List<JobTechStackRelation>> {

    @Autowired
    private TechStackService techStackService;

    @Override
    public void initialize(TechStacksValidation constraintAnnotation) { }

    @Override
    public boolean isValid(List<JobTechStackRelation> techStackRelations, ConstraintValidatorContext context) {
        return Optional.ofNullable(techStackRelations)
                .map(relations -> validateTechStackRelations(relations, context))
                .orElse(true);
    }

    private boolean validateTechStackRelations(List<JobTechStackRelation> techStackRelations, ConstraintValidatorContext context) {
        boolean isValid = true;
        List<Long> specifiedTechStackIds = getTechStackIds(techStackRelations);
        Set<Long> duplicatedTechStackIds = findDuplicatedTechStackIds(specifiedTechStackIds);
        List<Long> notExistingTechStackIds = findNotExistingTechStackIds(specifiedTechStackIds);
        String message = "";
        if(!duplicatedTechStackIds.isEmpty()) {
            isValid = false;
            message = MessageFormat.format("Specified duplicated tech stack id(s): {0}.", getCommaSeparatedIds(duplicatedTechStackIds.stream()));
        }
        if(!notExistingTechStackIds.isEmpty()) {
            isValid = false;
            message = MessageFormat.format("Given tech stack does not exists or was removed, id(s): {0}.", getCommaSeparatedIds(notExistingTechStackIds.stream()));
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return isValid;
    }

    private String getCommaSeparatedIds(Stream<Long> stream) {
        return stream
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    private List<Long> getTechStackIds(List<JobTechStackRelation> techStackRelations) {
        return techStackRelations.stream()
                .map(JobTechStackRelation::getTechStackId)
                .collect(Collectors.toList());
    }

    private Set<Long> findDuplicatedTechStackIds(List<Long> techStackIds) {
        return techStackIds.stream()
                .filter(id -> Collections.frequency(techStackIds, id) > 1)
                .collect(Collectors.toSet());
    }

    private List<Long> findNotExistingTechStackIds(List<Long> techStackIds) {
        List<Long> foundTechStackIds = findTechStacks(techStackIds);
        return techStackIds.stream()
                .filter(techStackId -> !foundTechStackIds.contains(techStackId))
                .collect(Collectors.toList());
    }

    private List<Long> findTechStacks(List<Long> techStackIds) {
        return techStackService.findByIds(techStackIds).stream()
                .map(TechStack::getId)
                .collect(Collectors.toList());
    }

}
