package getjobin.it.portal.jobservice.domain.techstack.control;

import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.infrastructure.query.boundary.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TechStackRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QueryService queryService;

    @Autowired
    public TechStackRepository(QueryService queryService) {
        this.queryService = queryService;
    }

    public Optional<TechStack> findById(Long techStackId) {
        return Optional.ofNullable(entityManager.find(TechStack.class, techStackId));
    }

    public List<TechStack> findByIds(List<Long> techStackIds) {
        return queryService.findEntitiesByIds(TechStack.class, techStackIds);
    }

    public TechStack getById(Long techStackId) {
        return findById(techStackId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Tech Stack with id: {0} does not exist or was removed", String.valueOf(techStackId))));
    }

    public Long save(TechStack techStack) {
        entityManager.persist(techStack);
        return techStack.getId();
    }

    public Long update(TechStack techStack) {
        return entityManager.merge(techStack).getId();
    }

    public void removeById(Long techStackId) {
        findById(techStackId).ifPresent(entityManager::remove);
    }

    public void remove(TechStack techStack) {
        entityManager.remove(techStack);
    }

}
