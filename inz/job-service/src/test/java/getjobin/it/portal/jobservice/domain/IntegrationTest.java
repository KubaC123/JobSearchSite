package getjobin.it.portal.jobservice.domain;

import getjobin.it.portal.jobservice.domain.category.control.CategoryService;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import getjobin.it.portal.jobservice.domain.category.entity.TestCategoryBuilder;
import getjobin.it.portal.jobservice.domain.company.control.CompanyService;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.company.entity.TestCompanyBuilder;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.TestJobBuilder;
import getjobin.it.portal.jobservice.domain.location.control.LocationService;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.domain.location.entity.TestLocationBuilder;
import getjobin.it.portal.jobservice.domain.technology.control.TechnologyService;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.technology.entity.TestTechnologyBuilder;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.domain.techstack.entity.TestTechStackBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IntegrationTest {

    private static final int NUMBER_OF_TECH_STACKS = 5;
    private static final int NUMBER_OF_LOCATIONS = 2;

    @Autowired
    private JobService jobService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TechnologyService technologyService;

    @Autowired
    private TechStackService techStackService;

    @Autowired
    private LocationService locationService;

    private Company company;
    private Category category;
    private Technology technology;
    private List<TechStack> techStacks;
    private List<Location> locations;

    public void init() {
        company = companyService.getById(companyService.create(TestCompanyBuilder.buildValidCompany()));
        category = categoryService.getById(categoryService.create(TestCategoryBuilder.buildValidCategory()));
        technology = technologyService.getById(technologyService.create(TestTechnologyBuilder.buildValidTechnology()));
        techStacks = TestTechStackBuilder.buildValidTechStacks(NUMBER_OF_TECH_STACKS).stream()
                .map(techStackService::create)
                .map(techStackService::getById)
                .collect(Collectors.toList());
        locations = TestLocationBuilder.buildValidLocations(NUMBER_OF_LOCATIONS).stream()
                .map(locationService::create)
                .map(locationService::getById)
                .collect(Collectors.toList());
    }

    public void clear() {
        companyService.remove(company);
        categoryService.remove(category);
        technologyService.remove(technology);
        techStacks.forEach(techStackService::remove);
        locations.forEach(locationService::remove);
    }

    public Company getCompany() {
        return company;
    }

    public Category getCategory() {
        return category;
    }

    public Technology getTechnology() {
        return technology;
    }

    public List<TechStack> getTechStacks() {
        return techStacks;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public Job createValidJob() {
        return jobService.getById(jobService.create(buildValidJob()));
    }

    public Job buildValidJob() {
        return TestJobBuilder.buildValidJobWith(company, category, technology, techStacks, locations);
    }

    public void createValidJobWith(Company company) {
        jobService.create(Job.toBuilder(buildValidJob())
                .company(company)
                .build());
    }

    public void createValidJobWith(Technology technoloy) {
        jobService.create(Job.toBuilder(buildValidJob())
                .technology(technoloy)
                .build());
    }

    public void createValidJobWith(Category category) {
        jobService.create(Job.toBuilder(buildValidJob())
                .category(category)
                .build());
    }

    public void createValidJobWithTechStacks(List<TechStack> techStacks) {
        jobService.create(Job.toBuilder(buildValidJob())
                .techStackRelations(TestJobBuilder.getTechStackRelations(techStacks))
                .build());
    }

    public void createValidJobWithLocations(List<Location> locations) {
        jobService.create(Job.toBuilder(buildValidJob())
                .locationRelations(TestJobBuilder.getLocationRelations(locations))
                .build());
    }

    public Job createValidJobWith(String title) {
        return jobService.getById(jobService.create(Job.toBuilder(buildValidJob())
                .title(title)
                .build()));
    }
}
