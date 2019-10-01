package getjobin.it.portal.jobservice.domain.category;

import getjobin.it.portal.jobservice.domain.category.control.CategoryRepository;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import getjobin.it.portal.jobservice.domain.category.entity.TestCategoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CategoryRepositoryUnitTest {

    private static final String UPDATE = "update";

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(categoryRepository);
    }

    @Test
    public void givenValidDataThenCreatesCategory() {
        Long categoryId = categoryRepository.saveCategory(TestCategoryBuilder.buildValidCategory());
        assertNotNull(categoryId);
    }

    @Test
    public void givenExistingCategoryThenFindsItById() {
        Long categoryId = categoryRepository.saveCategory(TestCategoryBuilder.buildValidCategory());
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);
        assertTrue(foundCategory.isPresent());
    }

    @Test
    public void givenExistingCategoryThenRemovesIt() {
        Long categoryId = categoryRepository.saveCategory(TestCategoryBuilder.buildValidCategory());
        categoryRepository.removeCategoryById(categoryId);
        Optional<Category> removedTechnology = categoryRepository.findById(categoryId);
        assertTrue(removedTechnology.isEmpty());
    }


    @Test
    public void givenValidDataOnUpdateThenUpdatesCategory() {
        Long categoryId = categoryRepository.saveCategory(TestCategoryBuilder.buildValidCategory());
        Category foundCategory = categoryRepository.getById(categoryId);
        Category updatedCategory = TestCategoryBuilder.buildValidUpdatedCategory(foundCategory);
        categoryRepository.updateCategory(updatedCategory);
        Category finalCategory = categoryRepository.getById(categoryId);
        assertEquals(TestCategoryBuilder.NAME + UPDATE, finalCategory.getName());
    }
}
