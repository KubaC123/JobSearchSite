package getjobin.it.portal.jobservice.infrastructure;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import getjobin.it.portal.jobservice.domain.company.control.CompanyRepository;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.company.entity.TestCompanyBuilder;
import getjobin.it.portal.jobservice.domain.job.control.JobRepository;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.TestJobBuilder;
import getjobin.it.portal.jobservice.domain.technology.control.TechnologyRepository;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.technology.entity.TestTechnologyBuilder;
import getjobin.it.portal.jobservice.infrastructure.exceptions.JobServiceIllegalArgumentException;
import getjobin.it.portal.jobservice.infrastructure.query.CustomRSQLVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RSQLUnitTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(jobRepository);
    }

    @Test
    public void givenJobTitleThenFindsIt() {
        jobRepository.saveJob(TestJobBuilder.buildValidJob());
        Node rootNode = new RSQLParser().parse("title=='" + TestJobBuilder.TITLE + "'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new CustomRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        foundJobs.forEach(job -> assertEquals(TestJobBuilder.TITLE, job.getTitle()));
    }

    @Test
    public void givenJobSalaryInRangeThenFindsIt() {
        Long createdJobId = jobRepository.saveJob(TestJobBuilder.buildValidJob());
        Node rootNode = new RSQLParser().parse("salaryMin>=" + TestJobBuilder.SALARY_MIN + ";salaryMax<=" + TestJobBuilder.SALARY_MAX);
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new CustomRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJobId.equals(job.getId())));
        foundJobs.forEach(job -> assertTrue(job.getSalaryMin() >= TestJobBuilder.SALARY_MIN && job.getSalaryMax() <= TestJobBuilder.SALARY_MAX));
    }

    @Test
    public void givenJobSalaryInSetThenFindsIt() {
        Long createdJobId = jobRepository.saveJob(TestJobBuilder.buildValidJob());
        Node rootNode = new RSQLParser().parse("salaryMin=in=(" + TestJobBuilder.SALARY_MIN + ",100,3200,4200)");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new CustomRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJobId.equals(job.getId())));
    }

    @Test
    public void givenJobTechnologyIdThenFindsIt() {
        Long technologyId = technologyRepository.saveTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology technology = technologyRepository.getById(technologyId);
        Long jobWithTechnologyId = jobRepository.saveJob(TestJobBuilder.buildValidJobWithTechnology(technology));
        Node rootNode = new RSQLParser().parse("technology.id==" + technologyId);
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new CustomRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> jobWithTechnologyId.equals(job.getId())));
        assertTrue(foundJobs.stream()
                .allMatch(job -> technology.equals(job.getTechnology())));
    }

    @Test
    public void givenJobTechnologyNameThenFindsIt() {
        Long technologyId = technologyRepository.saveTechnology(TestTechnologyBuilder.buildValidTechnology());
        Technology technology = technologyRepository.getById(technologyId);
        Long jobWithTechnologyId = jobRepository.saveJob(TestJobBuilder.buildValidJobWithTechnology(technology));
        Node rootNode = new RSQLParser().parse("technology.name==" + technology.getName());
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new CustomRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> jobWithTechnologyId.equals(job.getId())));
        assertTrue(foundJobs.stream()
                .allMatch(job -> technology.equals(job.getTechnology())));
    }

    @Test
    public void givenJobCompanyIdThenFindsIt() {
        Long companyId = companyRepository.saveCompany(TestCompanyBuilder.buildValidCompany());
        Company company = companyRepository.getById(companyId);
        Long jobWithCompanyId = jobRepository.saveJob(TestJobBuilder.buildValidJobInCompany(company));
        Node rootNode = new RSQLParser().parse("company.id==" + companyId);
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new CustomRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> jobWithCompanyId.equals(job.getId())));
        assertTrue(foundJobs.stream()
                .allMatch(job -> company.equals(job.getCompany())));
    }

    @Test
    public void givenJobCompanyNameThenFindsIt() {
        Long companyId = companyRepository.saveCompany(TestCompanyBuilder.buildValidCompany());
        Company company = companyRepository.getById(companyId);
        Long jobWithCompanyId = jobRepository.saveJob(TestJobBuilder.buildValidJobInCompany(company));
        Node rootNode = new RSQLParser().parse("company.name=='" + company.getName() + "'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new CustomRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> jobWithCompanyId.equals(job.getId())));
        assertTrue(foundJobs.stream()
                .allMatch(job -> company.equals(job.getCompany())));
    }

    @Test
    public void givenInactiveJobIdAndActiveTrueConditionThenReturnsEmptyList() {
        Long createdJobId = jobRepository.saveJob(TestJobBuilder.buildValidJob());
        jobRepository.removeJobById(createdJobId);
        Node rootNode = new RSQLParser().parse("active==true;id==" + createdJobId);
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new CustomRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.isEmpty());
    }

    @Test
    public void givenJobTitleAndActiveTrueThenFindsIt() {
        Long createdJobId = jobRepository.saveJob(TestJobBuilder.buildValidJob());
        Node rootNode = new RSQLParser().parse("active==true;title=='" + TestJobBuilder.TITLE + "'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new CustomRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJobId.equals(job.getId())));
    }

    @Test
    public void givenInactiveJobIdThenFindsIt() {
        Long createdJobId = jobRepository.saveJob(TestJobBuilder.buildValidJob());
        jobRepository.removeJobById(createdJobId);
        Node rootNode = new RSQLParser().parse("id==" + createdJobId);
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new CustomRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertEquals(1, foundJobs.size());
        assertEquals(createdJobId, foundJobs.get(0).getId());
    }

    @Test(expected = JobServiceIllegalArgumentException.class)
    public void givenDoubleNestedPropertyThenThrowsJobServiceIllegalArgumentException() {
        Node rootNode = new RSQLParser().parse("technology.otherattribute.id==1232");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new CustomRSQLVisitor<>());
        jobRepository.findBySpecification(rsqlBasedSpecification);
    }
}
