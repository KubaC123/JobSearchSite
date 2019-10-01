package getjobin.it.portal.jobservice.domain.category.entity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestCategoryBuilder {

    public static final String NAME = "Architect";
    public static final String UPDATE = "update";

    public static Category buildValidCategory() {
        return Category.builder()
                .withName(NAME)
                .build();
    }

    public static List<Category> buildValidCategories(int numberOfCategories) {
        return IntStream.rangeClosed(1, numberOfCategories)
                .mapToObj(index -> Category.builder()
                        .withName(NAME + index)
                        .build())
                .collect(Collectors.toList());
    }

    public static Category buildCategoryWithEmptyName() {
        return Category.builder()
                .withName("")
                .build();
    }

    public static Category buildCategoryWithNullName() {
        return Category.builder()
                .build();
    }

    public static Category buildValidUpdatedCategory(Category createdCategory) {
        return Category.toBuilder(createdCategory)
                .withName(NAME + UPDATE)
                .build();
    }
}
