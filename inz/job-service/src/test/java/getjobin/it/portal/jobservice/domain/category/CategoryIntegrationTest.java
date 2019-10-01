package getjobin.it.portal.jobservice.domain.category;

import getjobin.it.portal.jobservice.domain.category.control.CategoryService;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import getjobin.it.portal.jobservice.domain.category.entity.TestCategoryBuilder;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.TestJobBuilder;
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
    private JobService jobService;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(categoryService);
        assertNotNull(jobService);
    }

    @Test(expected = ConstraintViolationException.class)
    public void givenCategoryWithActiveJobsOnRemoveThenThrowsConstraintViolationException() {
        Long categoryId = categoryService.create(TestCategoryBuilder.buildValidCategory());
        Category createdCategory = categoryService.getById(categoryId);
        jobService.create(TestJobBuilder.buildValidJobWithCategory(createdCategory));
        categoryService.remove(createdCategory);
    }
}
