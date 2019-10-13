package getjobin.it.portal.jobservice.domain.category;

import getjobin.it.portal.jobservice.domain.IntegrationTest;
import getjobin.it.portal.jobservice.domain.category.control.CategoryService;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import getjobin.it.portal.jobservice.domain.category.entity.TestCategoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CategoryIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IntegrationTest integrationTest;

    @Before
    public void init() {
        integrationTest.init();
    }

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(categoryService);
        assertNotNull(integrationTest);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenCategoryWithActiveJobsOnRemoveThenThrowsConstraintViolationException() {
        Long categoryId = categoryService.create(TestCategoryBuilder.buildValidCategory());
        Category createdCategory = categoryService.getById(categoryId);
        integrationTest.createValidJobWith(createdCategory);
        categoryService.remove(createdCategory);
    }
}
