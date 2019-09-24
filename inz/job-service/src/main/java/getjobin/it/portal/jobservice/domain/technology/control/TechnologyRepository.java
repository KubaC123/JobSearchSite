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
        return queryService.findEntitiesByIds(Technology.class, technologyIds);
    }

    public Technology getById(Long technologyId) {
        return findById(technologyId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Technology with id: {0} does not exist or was removed", String.valueOf(technologyId))));
    }

    public Long saveTechnology(Technology technology) {
        entityManager.persist(technology);
        return technology.getId();
    }

    public Long updateTechnology(Technology techStack) {
        return entityManager.merge(techStack).getId();
    }

    public void removeTechnologyById(Long techStackId) {
        findById(techStackId).ifPresent(entityManager::remove);
    }

    public void removeTechnology(Technology techStack) {
        entityManager.remove(techStack);
    }

}
