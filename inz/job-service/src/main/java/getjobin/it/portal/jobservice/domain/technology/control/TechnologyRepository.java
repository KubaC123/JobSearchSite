package getjobin.it.portal.jobservice.domain.technology.control;

import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
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
public class TechnologyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QueryService queryService;

    @Autowired
    public TechnologyRepository(QueryService queryService) {
        this.queryService = queryService;
    }

    public Optional<Technology> findById(Long technologyId) {
        return Optional.ofNullable(entityManager.find(Technology.class, technologyId));
    }

    public List<Technology> findByIds(List<Long> technologyIds) {
        return queryService.queryByIds(Technology.class, technologyIds);
    }

    public Technology getById(Long technologyId) {
        return findById(technologyId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Technology with id: {0} does not exist or was removed", String.valueOf(technologyId))));
    }

    public Long save(Technology technology) {
        entityManager.persist(technology);
        return technology.getId();
    }

    public Long update(Technology technology) {
        return entityManager.merge(technology).getId();
    }

    public void removeById(Long technologyId) {
        findById(technologyId).ifPresent(entityManager::remove);
    }

    public void remove(Technology technology) {
        entityManager.remove(technology);
    }

}
