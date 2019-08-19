package getjobin.it.portal.jobservice.domain.techstack.control;

import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.infrastructure.query.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TechStackRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private Validator validator;
    private QueryService queryService;

    @Autowired
    public TechStackRepository(Validator validator, QueryService queryService) {
        this.validator = validator;
        this.queryService = queryService;
    }

    public TechStack findById(Long techStackId) {
        return entityManager.find(TechStack.class, techStackId);
    }

    public List<TechStack> findByIds(List<Long> techStackIds) {
        return queryService.findEntitiesByIds(TechStack.class, techStackIds);
    }

    @Transactional
    public List<Long> createTechStacks(List<TechStack> techStacks) {
        return techStacks.stream()
                .map(this::createTechStack)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createTechStack(TechStack techStack) {
        validate(techStack);
        entityManager.persist(techStack);
        return techStack.getId();
    }

    private void validate(TechStack techStack) {
        Set<ConstraintViolation<TechStack>> violations = validator.validate(techStack);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @Transactional
    public Long updateTechStack(TechStack techStack) {
        validate(techStack);
        return entityManager.merge(techStack).getId();
    }

    @Transactional
    public void removeTechStackById(Long techStackId) {
        Optional.ofNullable(findById(techStackId))
                .ifPresent(entityManager::remove);
    }

    @Transactional
    public void removeTechStack(TechStack techStack) {
        entityManager.remove(techStack);
    }

}
