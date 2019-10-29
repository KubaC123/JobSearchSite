package getjobin.it.portal.jobservice.domain.search.boundary;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    public <T extends ManagedEntity> List<T> findAll(Class<T> entityClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(root);
        return entityManager.createQuery(all).getResultList();
    }

    public <T extends ManagedEntity> List<T> queryByIds(List<Long> ids, Class<T> entityClass) {
        return entityManager.unwrap(Session.class)
                .byMultipleIds(entityClass)
                .enableSessionCheck(true)
                .multiLoad(ids)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public <T extends ManagedEntity> Long countObjects(Class<T> entityClass) {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(entityClass)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    public <T extends ManagedEntity> List<T> queryPartition(Integer first, Integer last, Class<T> entityClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(root);
        return entityManager.createQuery(all)
                .setFirstResult(first)
                .setMaxResults(last)
                .getResultList();
    }
}
