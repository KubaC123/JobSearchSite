package getjobin.it.portal.jobservice.domain.category.boundary;

import getjobin.it.portal.jobservice.api.CategoryDto;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class CategoryMapper {

    public Category toEntity(CategoryDto categoryDTO) {
        return Category.builder()
                .withId(categoryDTO.getId())
                .withName(categoryDTO.getName())
                .build();
    }

    List<CategoryDto> toDtos(List<Category> categories) {
        return categories.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    Category updateExistingCategory(Category existingCategory, CategoryDto categoryDTO) {
        return Category.toBuilder(existingCategory)
                .withName(categoryDTO.getName())
                .build();
    }
}
