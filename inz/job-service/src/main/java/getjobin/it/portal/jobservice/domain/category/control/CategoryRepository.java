package getjobin.it.portal.jobservice.domain.category.control;

import getjobin.it.portal.jobservice.domain.category.entity.Category;
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
public class CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QueryService queryService;

    @Autowired
    public CategoryRepository(QueryService queryService) {
        this.queryService = queryService;
    }

    public Optional<Category> findById(Long categoryId) {
        return Optional.ofNullable(entityManager.find(Category.class, categoryId));
    }

    public List<Category> findByIds(List<Long> categoryIds) {
        return queryService.queryByIds(Category.class, categoryIds);
    }

    public Category getById(Long categoryId) {
        return findById(categoryId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Category with id: {0} does not exist or was removed", String.valueOf(categoryId))));
    }

    public Long save(Category category) {
        entityManager.persist(category);
        return category.getId();
    }

    public Long update(Category category) {
        return entityManager.merge(category).getId();
    }

    public void removeById(Long categoryId) {
        findById(categoryId).ifPresent(entityManager::remove);
    }

    public void remove(Category category) {
        entityManager.remove(category);
    }

}
