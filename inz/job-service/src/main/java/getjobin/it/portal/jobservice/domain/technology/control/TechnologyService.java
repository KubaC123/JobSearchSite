package getjobin.it.portal.jobservice.domain.technology.control;

import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TechnologyService {

    private Validator validator;
    private TechnologyRepository technologyRepository;

    @Autowired
    public TechnologyService(Validator validator, TechnologyRepository technologyRepository) {
        this.validator = validator;
        this.technologyRepository = technologyRepository;
    }

    public List<Technology> findByIds(List<Long> technologyIds) {
        return technologyRepository.findByIds(technologyIds);
    }

    public Optional<Technology> findById(Long technologyId) {
        return technologyRepository.findById(technologyId);
    }

    public Technology getById(Long technologyId) {
        return technologyRepository.getById(technologyId);
    }

    public Long create(Technology technology) {
        validate(technology);
        return technologyRepository.save(technology);
    }

    private void validate(Technology technology) {
        Set<ConstraintViolation<Technology>> violations = validator.validate(technology);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Long update(Technology technology) {
        validate(technology);
        return technologyRepository.update(technology);
    }

    public void incrementJobCounter(Technology technology) {
        Integer currentCount = technology.getJobCounter();
        technology.setJobCounter(++currentCount);
        technologyRepository.update(technology);
    }

    public void decrementJobCounter(Technology technology) {
        Integer currentCount = technology.getJobCounter();
        technology.setJobCounter(--currentCount);
        technologyRepository.update(technology);
    }

    public void remove(Technology technology) {
        validateOnRemove(technology);
        technologyRepository.remove(technology);
    }

    private void validateOnRemove(Technology technology) {
        Set<ConstraintViolation<Technology>> violations = validator.validate(technology, Technology.DeleteValidations.class);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
