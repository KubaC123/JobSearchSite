package getjobin.it.portal.jobservice.infrastructure;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
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
import getjobin.it.portal.jobservice.infrastructure.exception.JobServiceException;
import getjobin.it.portal.jobservice.infrastructure.query.boundary.ManagedEntityRSQLVisitor;
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
public class RSQLSearchTest {

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
        jobRepository.save(TestJobBuilder.buildValidJob());
        Node rootNode = new RSQLParser().parse("title=='" + TestJobBuilder.TITLE + "'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        foundJobs.forEach(job -> assertEquals(TestJobBuilder.TITLE, job.getTitle()));
    }

    @Test
    public void givenJobSalaryInRangeThenFindsIt() {
        Long createdJobId = jobRepository.save(TestJobBuilder.buildValidJob());
        Node rootNode = new RSQLParser().parse("salaryMin>=" + TestJobBuilder.SALARY_MIN + ";salaryMax<=" + TestJobBuilder.SALARY_MAX);
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJobId.equals(job.getId())));
        foundJobs.forEach(job -> assertTrue(job.getSalaryMin() >= TestJobBuilder.SALARY_MIN && job.getSalaryMax() <= TestJobBuilder.SALARY_MAX));
    }

    @Test
    public void givenJobSalaryInSetThenFindsIt() {
        Long createdJobId = jobRepository.save(TestJobBuilder.buildValidJob());
        Node rootNode = new RSQLParser().parse("salaryMin=in=(" + TestJobBuilder.SALARY_MIN + ",100,3200,4200)");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJobId.equals(job.getId())));
    }

    @Test
    public void givenJobSalaryInOutSetThenNotReturnIt() {
        Long createdJobId = jobRepository.save(TestJobBuilder.buildValidJob());
        Node rootNode = new RSQLParser().parse("salaryMin=out=(" + TestJobBuilder.SALARY_MIN + ",100,3200,4200)");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.stream()
                .noneMatch(job -> createdJobId.equals(job.getId())));
    }

    @Test
    public void givenJobTechnologyIdThenFindsIt() {
        Long technologyId = technologyRepository.save(TestTechnologyBuilder.buildValidTechnology());
        Technology technology = technologyRepository.getById(technologyId);
        Long jobWithTechnologyId = jobRepository.save(TestJobBuilder.buildValidJobWithTechnology(technology));
        Node rootNode = new RSQLParser().parse("technology.id==" + technologyId);
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> jobWithTechnologyId.equals(job.getId())));
        assertTrue(foundJobs.stream()
                .allMatch(job -> technology.equals(job.getTechnology())));
    }

    @Test
    public void givenJobTechnologyNameThenFindsIt() {
        Long technologyId = technologyRepository.save(TestTechnologyBuilder.buildValidTechnology());
        Technology technology = technologyRepository.getById(technologyId);
        Long jobWithTechnologyId = jobRepository.save(TestJobBuilder.buildValidJobWithTechnology(technology));
        Node rootNode = new RSQLParser().parse("technology.name==" + technology.getName());
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> jobWithTechnologyId.equals(job.getId())));
        assertTrue(foundJobs.stream()
                .allMatch(job -> technology.equals(job.getTechnology())));
    }

    @Test
    public void givenJobCompanyIdThenFindsIt() {
        Long companyId = companyRepository.save(TestCompanyBuilder.buildValidCompany());
        Company company = companyRepository.getById(companyId);
        Long jobWithCompanyId = jobRepository.save(TestJobBuilder.buildValidJobInCompany(company));
        Node rootNode = new RSQLParser().parse("company.id==" + companyId);
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> jobWithCompanyId.equals(job.getId())));
        assertTrue(foundJobs.stream()
                .allMatch(job -> company.equals(job.getCompany())));
    }

    @Test
    public void givenJobCompanyNameThenFindsIt() {
        Long companyId = companyRepository.save(TestCompanyBuilder.buildValidCompany());
        Company company = companyRepository.getById(companyId);
        Long jobWithCompanyId = jobRepository.save(TestJobBuilder.buildValidJobInCompany(company));
        Node rootNode = new RSQLParser().parse("company.name=='" + company.getName() + "'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> jobWithCompanyId.equals(job.getId())));
        assertTrue(foundJobs.stream()
                .allMatch(job -> company.equals(job.getCompany())));
    }

    @Test
    public void givenInactiveJobIdAndActiveTrueConditionThenReturnsEmptyList() {
        Long createdJobId = jobRepository.save(TestJobBuilder.buildValidJob());
        jobRepository.removeById(createdJobId);
        Node rootNode = new RSQLParser().parse("active==true;id==" + createdJobId);
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.isEmpty());
    }

    @Test
    public void givenJobTitleMatchingWithDoubleWildcardThenFindsIt() {
        Long createdJobId = jobRepository.save(TestJobBuilder.buildValidJobWithTitle("Here java is present"));
        Node rootNode = new RSQLParser().parse("title=='*java*'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJobId.equals(job.getId())));
    }

    @Test
    public void givenJobTitleMatchingWithSingleWildcardThenFindsIt() {
        Long createdJobId = jobRepository.save(TestJobBuilder.buildValidJobWithTitle("Junior dev java"));
        Node rootNode = new RSQLParser().parse("title=='*java'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJobId.equals(job.getId())));
    }

    @Test
    public void givenInactiveJobIdThenFindsIt() {
        Long createdJobId = jobRepository.save(TestJobBuilder.buildValidJob());
        jobRepository.removeById(createdJobId);
        Node rootNode = new RSQLParser().parse("id==" + createdJobId);
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertEquals(1, foundJobs.size());
        assertEquals(createdJobId, foundJobs.get(0).getId());
    }

    @Test(expected = JobServiceException.class)
    public void givenDoubleNestedPropertyThenThrowsJobServiceException() {
        Node rootNode = new RSQLParser().parse("technology.otherattribute.id==1232");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        jobRepository.findBySpecification(rsqlBasedSpecification);
    }

    @Test(expected = RSQLParserException.class)
    public void givenInvalidSyntaxThenThrowsRSQLParserException() {
        Node rootNode = new RSQLParser().parse("technology=lik=='test';title='test'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        jobRepository.findBySpecification(rsqlBasedSpecification);
    }

    @Test(expected = Exception.class)
    public void givenNotExistingJobAttributeThenThrowsException() {
        Node rootNode = new RSQLParser().parse("randomAttr=='test';drugiRandomAttr=='test'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        jobRepository.findBySpecification(rsqlBasedSpecification);
    }
}
