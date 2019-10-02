package getjobin.it.portal.jobservice.domain.indexation;

import com.fasterxml.jackson.databind.ObjectMapper;
import getjobin.it.portal.jobservice.domain.category.control.CategoryService;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import getjobin.it.portal.jobservice.domain.category.entity.TestCategoryBuilder;
import getjobin.it.portal.jobservice.domain.company.control.CompanyService;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.company.entity.TestCompanyBuilder;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.TestJobBuilder;
import getjobin.it.portal.jobservice.domain.indexation.boundary.IndexationMapper;
import getjobin.it.portal.jobservice.domain.technology.control.TechnologyService;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.technology.entity.TestTechnologyBuilder;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.domain.techstack.entity.TestTechStackBuilder;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class IndexationMapperUnitTest {

    private Job job;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TechnologyService technologyService;

    @Autowired
    private TechStackService techStackService;

    @Autowired
    private JobService jobService;

    @Autowired
    private IndexationMapper indexationMapper;

    @Before
    public void init() {
        Company company = companyService.getById(companyService.create(TestCompanyBuilder.buildValidCompany()));
        Category category = categoryService.getById(categoryService.create(TestCategoryBuilder.buildValidCategory()));
        Technology technology = technologyService.getById(technologyService.create(TestTechnologyBuilder.buildValidTechnology()));
        List<TechStack> techStacks = TestTechStackBuilder.buildValidTechStacks(5).stream()
                .map(techStackService::create)
                .map(techStackService::getById)
                .collect(Collectors.toList());
        job = jobService.getById(jobService.create(TestJobBuilder.buildValidJobWith(company, category, technology, techStacks)));
    }

    @Test
    public void givenJobIndexationDTOThenParseToJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        Assertions.assertThatCode(() -> objectMapper.writeValueAsString(indexationMapper.toJobIndexationDTO(job)))
                .doesNotThrowAnyException();
    }
}
