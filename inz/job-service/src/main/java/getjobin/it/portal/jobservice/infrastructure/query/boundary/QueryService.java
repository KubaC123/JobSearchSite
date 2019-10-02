package getjobin.it.portal.jobservice.infrastructure.query.boundary;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class QueryService {

    @PersistenceContext
    private EntityManager entityManager;

    public <T extends ManagedEntity> List<T> queryByIds(Class<T> entityClass, List<Long> ids) {
        return entityManager.unwrap(Session.class)
                .byMultipleIds(entityClass)
                .enableSessionCheck(true)
                .multiLoad(ids)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public <T extends ManagedEntity> List<T> findAll(Class<T> entityClass) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        CriteriaQuery<T> all = criteriaQuery.select(root);
        TypedQuery<T> query = entityManager.createQuery(all);
        return query.getResultList();
    }
}
