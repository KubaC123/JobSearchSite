package getjobin.it.portal.jobservice.domain.search.boundary;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.annotations.common.reflection.ReflectionUtil;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
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

    public <T extends ManagedEntity> List<T> queryPartition(Integer startRow, Integer endRow, Class<T> entityClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(root);

        List<T> results = entityManager.createQuery(all)
                .setFirstResult(startRow)
                .setMaxResults(endRow)
                .getResultList();

        results.forEach(Hibernate::unproxy);
        results.forEach(Hibernate::initialize);

//        Field[] fields = entityClass.getDeclaredFields();
//        for(Field field : fields) {
//            if(Collection.class.isAssignableFrom(field.getType())) {
//                results.forEach(result -> tryInvokeGetterOnCollectionField(result, field.getName()));
//            }
//        }

        return results;
    }

//    /**
//     * This is highly experimental, used to invoke getter on collection type fields mapped using LAZY loading
//     * @param object - object on which field is present
//     * @param collectionFieldName - name of collection type field
//     * @param <T> - domain known managed entity type
//     */
//    public <T extends ManagedEntity> void tryInvokeGetterOnCollectionField(T object, String collectionFieldName) {
//        try {
//            ReflectivePropertyAccessor reflectivePropertyAccessor = new ReflectivePropertyAccessor();
//
//        } catch (IllegalAccessException | IllegalArgumentException
//                | InvocationTargetException | IntrospectionException exception) {
//            log.warn("[QUERY SERVICE] exception during invoking getter on collection");
//        }
//    }
}
