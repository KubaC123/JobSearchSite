package getjobin.it.portal.jobservice.infrastructure.query;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class QueryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public <T extends ManagedEntity> List<T> findEntitiesByIds(Class<T> entityClass, List<Long> ids) {
        List<T> result = entityManager.unwrap(Session.class)
                .byMultipleIds(entityClass)
                .enableSessionCheck(true)
                .multiLoad(ids);
        if(result.stream().allMatch(Objects::isNull)) {
            return Collections.emptyList();
        }
        return result;
    }
}
