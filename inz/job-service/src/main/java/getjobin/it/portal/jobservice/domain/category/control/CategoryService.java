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

    private Validator validator;
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(Validator validator, CategoryRepository categoryRepository) {
        this.validator = validator;
        this.categoryRepository = categoryRepository;
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

    public Long createCategory(Category category) {
        validate(category);
        return categoryRepository.saveCategory(category);
    }

    private void validate(Category technology) {
        Set<ConstraintViolation<Category>> violations = validator.validate(technology);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Long updateCategory(Category technology) {
        validate(technology);
        return categoryRepository.updateCategory(technology);
    }

    public void removeCategory(Category technology) {
        validateOnRemove(technology);
        categoryRepository.removeCategory(technology);
    }

    private void validateOnRemove(Category category) {
        Set<ConstraintViolation<Category>> violations = validator.validate(category, Category.DeleteValidations.class);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
