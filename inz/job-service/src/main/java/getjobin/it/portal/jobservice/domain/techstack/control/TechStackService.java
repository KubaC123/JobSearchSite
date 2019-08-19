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

    public List<Long> createTechStacks(List<TechStack> techStacks) {
        techStacks.forEach(this::validate);
        return techStackRepository.saveTechStacks(techStacks);
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

    public void deleteTechStacksByIds(List<Long> techStacksIds) {
        techStacksIds.forEach(techStackRepository::removeTechStackById);
    }

}
