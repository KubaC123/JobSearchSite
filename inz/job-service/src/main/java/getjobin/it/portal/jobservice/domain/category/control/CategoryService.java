package getjobin.it.portal.jobservice.domain.category.control;

import getjobin.it.portal.jobservice.domain.category.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    private Validator validator;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findByIds(List<Long> categoryIds) {
        return categoryRepository.findByIds(categoryIds);
    }

    public Optional<Category> findById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public Category getById(Long categoryId) {
        return categoryRepository.getById(categoryId);
    }

    public Long create(Category category) {
        validate(category);
        return categoryRepository.save(category);
    }

    private void validate(Category category) {
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Long update(Category category) {
        validate(category);
        return categoryRepository.update(category);
    }

    public void remove(Category category) {
        validateOnRemove(category);
        categoryRepository.remove(category);
    }

    private void validateOnRemove(Category category) {
        Set<ConstraintViolation<Category>> violations = validator.validate(category, Category.DeleteValidations.class);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
