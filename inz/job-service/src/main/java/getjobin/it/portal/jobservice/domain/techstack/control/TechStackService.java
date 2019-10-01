package getjobin.it.portal.jobservice.domain.techstack.control;

import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public Long create(TechStack techStack) {
        validate(techStack);
        return techStackRepository.save(techStack);
    }

    private void validate(TechStack techStack) {
        Set<ConstraintViolation<TechStack>> violations = validator.validate(techStack);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Long update(TechStack techStack) {
        validate(techStack);
        return techStackRepository.update(techStack);
    }

    public void remove(TechStack techStack) {
        validateOnRemove(techStack);
        techStackRepository.remove(techStack);
    }

    private void validateOnRemove(TechStack techStack) {
        Set<ConstraintViolation<TechStack>> violations = validator.validate(techStack, TechStack.DeleteValidations.class);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
