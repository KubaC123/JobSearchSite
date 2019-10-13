package getjobin.it.portal.jobservice.domain.category.boundary;

import getjobin.it.portal.jobservice.api.CategoryDto;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryDto categoryDTO) {
        return Category.builder()
                .withId(categoryDTO.getId())
                .withName(categoryDTO.getName())
                .build();
    }

    public CategoryDto toDTO(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category updateExistingCategory(Category existingCategory, CategoryDto categoryDTO) {
        return Category.toBuilder(existingCategory)
                .withName(categoryDTO.getName())
                .build();
    }
}
