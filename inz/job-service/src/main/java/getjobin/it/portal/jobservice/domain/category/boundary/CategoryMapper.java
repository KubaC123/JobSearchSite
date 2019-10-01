package getjobin.it.portal.jobservice.domain.category.boundary;

import getjobin.it.portal.jobservice.api.domain.rest.CategoryDTO;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryDTO categoryDTO) {
        return Category.builder()
                .withId(categoryDTO.getId())
                .withName(categoryDTO.getName())
                .build();
    }

    public CategoryDTO toDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .jobCounter(category.getJobCounter())
                .build();
    }

    public Category updateExistingCategory(Category existingCategory, CategoryDTO categoryDTO) {
        return Category.toBuilder(existingCategory)
                .withName(categoryDTO.getName())
                .build();
    }
}
