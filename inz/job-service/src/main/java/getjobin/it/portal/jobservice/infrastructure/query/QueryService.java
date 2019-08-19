package getjobin.it.portal.jobservice.infrastructure.query;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class QueryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public <T extends ManagedEntity> List<T> findEntitiesByIds(Class<T> entityClass, List<Long> ids) {
        return entityManager.unwrap(Session.class)
                .byMultipleIds(entityClass)
                .enableSessionCheck(true)
                .multiLoad(ids);
    }
}
