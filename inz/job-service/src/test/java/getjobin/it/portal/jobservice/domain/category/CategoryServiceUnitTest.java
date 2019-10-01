package getjobin.it.portal.jobservice.domain.category;

import getjobin.it.portal.jobservice.domain.category.control.CategoryService;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import getjobin.it.portal.jobservice.domain.category.entity.TestCategoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CategoryServiceUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private CategoryService categoryService;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(categoryService);
    }

    @Test
    public void givenValidDataThenCreatesCategory() {
        Long createdCategoryId = categoryService.createCategory(TestCategoryBuilder.buildValidCategory());
        Category createdCategory = categoryService.getById(createdCategoryId);
        assertEquals(createdCategoryId, createdCategory.getId());
    }

    @Test
    public void givenExistingCategoryThenFindsItById() {
        Long categoryId = categoryService.createCategory(TestCategoryBuilder.buildValidCategory());
        Optional<Category> foundCategory = categoryService.findById(categoryId);
        assertTrue(foundCategory.isPresent());
    }

    @Test
    public void givenValidDataOnUpdateThenUpdatesCategory() {
        Long categoryId = categoryService.createCategory(TestCategoryBuilder.buildValidCategory());
        Category foundCategory = categoryService.getById(categoryId);
        Category updatedCategory = TestCategoryBuilder.buildValidUpdatedCategory(foundCategory);
        categoryService.updateCategory(updatedCategory);
        Category finalCategory = categoryService.getById(categoryId);
        assertEquals(TestCategoryBuilder.NAME + UPDATE, finalCategory.getName());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenEmptyNameOnCreateThenThrowsConstraintViolationException() {
        categoryService.createCategory(TestCategoryBuilder.buildCategoryWithEmptyName());
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenNullNameOnCreateThenThrowsConstraintViolationException() {
        categoryService.createCategory(TestCategoryBuilder.buildCategoryWithNullName());
    }
}
