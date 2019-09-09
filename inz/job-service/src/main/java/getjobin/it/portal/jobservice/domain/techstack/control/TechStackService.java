package getjobin.it.portal.jobservice.domain.techstack.control;

import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.infrastructure.exceptions.JobServicePreconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TechStackService {

    private Validator validator;
    private TechStackRepository techStackRepository;

    @Autowired
    public TechStackService(Validator validator, TechStackRepository techStackRepository) {
        this.validator = validator;
        this.techStackRepository = techStackRepository;
    }

    public List<TechStack> findByIds(List<Long> techStackIds) {
        return techStackRepository.findByIds(techStackIds);
    }

    public Optional<TechStack> findById(Long techStackId) {
        return techStackRepository.findById(techStackId);
    }

    public TechStack getById(Long techStackId) {
        return techStackRepository.getById(techStackId);
    }

    public Long createTechStack(TechStack techStack) {
        validate(techStack);
        return techStackRepository.saveTechStack(techStack);
    }

    private void validate(TechStack techStack) {
        Set<ConstraintViolation<TechStack>> violations = validator.validate(techStack);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Long updateTechStack(TechStack techStack) {
        validate(techStack);
        return techStackRepository.updateTechStack(techStack);
    }

    public void removeTechStack(TechStack techStack) {
        techStackRepository.removeTechStack(techStack);
    }

    public void validateTechStackExistence(List<Long> techStackIds) {
        List<Long> foundTechStacks = findByIds(techStackIds).stream()
                .map(TechStack::getId)
                .collect(Collectors.toList());
        List<Long> notExistingTechStackIds = new ArrayList<>(techStackIds);
        notExistingTechStackIds.removeAll(foundTechStacks);
        JobServicePreconditions.checkArgument(notExistingTechStackIds.isEmpty(),
                MessageFormat.format("Some of specified tech stacks does not exist or was removed. Ids: {0}", getCommaSeparatedTechStackIds(notExistingTechStackIds)));
    }

    private String getCommaSeparatedTechStackIds(List<Long> notExistingTechStackIds) {
        return notExistingTechStackIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
