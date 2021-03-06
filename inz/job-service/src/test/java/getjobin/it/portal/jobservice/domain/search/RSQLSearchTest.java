package getjobin.it.portal.jobservice.domain.search;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.ast.Node;
import getjobin.it.portal.jobservice.domain.IntegrationTest;
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
import getjobin.it.portal.jobservice.domain.search.boundary.ManagedEntityRSQLVisitor;
import org.junit.After;
import org.junit.Before;
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

    @Autowired
    private IntegrationTest integrationTest;

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(integrationTest);
    }

    @Before
    public void init() {
        integrationTest.init();
    }

    @Test
    public void givenJobTitleThenFindsIt() {
        integrationTest.createValidJob();
        Node rootNode = new RSQLParser().parse("title=='" + TestJobBuilder.TITLE + "'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        foundJobs.forEach(job -> assertEquals(TestJobBuilder.TITLE, job.getTitle()));
    }

    @Test
    public void givenJobSalaryInRangeThenFindsIt() {
        Job createdJob = integrationTest.createValidJob();
        Node rootNode = new RSQLParser().parse("salaryMin>=" + TestJobBuilder.SALARY_MIN + ";salaryMax<=" + TestJobBuilder.SALARY_MAX);
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJob.getId().equals(job.getId())));
        foundJobs.forEach(job -> assertTrue(job.getSalaryMin() >= TestJobBuilder.SALARY_MIN && job.getSalaryMax() <= TestJobBuilder.SALARY_MAX));
    }

    @Test
    public void givenJobSalaryInSetThenFindsIt() {
        Job createdJob = integrationTest.createValidJob();
        Node rootNode = new RSQLParser().parse("salaryMin=in=(" + TestJobBuilder.SALARY_MIN + ",100,3200,4200)");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJob.getId().equals(job.getId())));
    }

    @Test
    public void givenJobSalaryInOutSetThenNotReturnIt() {
        Job createdJob = integrationTest.createValidJob();
        Node rootNode = new RSQLParser().parse("salaryMin=out=(" + TestJobBuilder.SALARY_MIN + ",100,3200,4200)");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.stream()
                .noneMatch(job -> createdJob.getId().equals(job.getId())));
    }

    @Test
    public void givenJobTechnologyIdThenFindsIt() {
        Job createdJob = integrationTest.createValidJob();
        Node rootNode = new RSQLParser().parse("technology.id==" + createdJob.getTechnology().getId());
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJob.getId().equals(job.getId())));
        assertTrue(foundJobs.stream()
                .allMatch(job -> createdJob.getTechnology().equals(job.getTechnology())));
    }

    @Test
    public void givenJobTechnologyNameThenFindsIt() {
        Job createdJob = integrationTest.createValidJob();
        Node rootNode = new RSQLParser().parse("technology.name==" + createdJob.getTechnology().getName());
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJob.getId().equals(job.getId())));
        assertTrue(foundJobs.stream()
                .allMatch(job -> createdJob.getTechnology().getName().equals(job.getTechnology().getName())));
    }

    @Test
    public void givenJobCompanyIdThenFindsIt() {
        Job createdJob = integrationTest.createValidJob();
        Node rootNode = new RSQLParser().parse("company.id==" + createdJob.getCompany().getId());
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJob.getId().equals(job.getId())));
        assertTrue(foundJobs.stream()
                .allMatch(job -> createdJob.getCompany().equals(job.getCompany())));
    }

    @Test
    public void givenJobCompanyNameThenFindsIt() {
        Job createdJob = integrationTest.createValidJob();
        Node rootNode = new RSQLParser().parse("company.name=='" + createdJob.getCompany().getName() + "'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.size() >= 1);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJob.getId().equals(job.getId())));
        assertTrue(foundJobs.stream()
                .allMatch(job -> createdJob.getCompany().getName().equals(job.getCompany().getName())));
    }

    @Test
    public void givenInactiveJobIdAndActiveTrueConditionThenReturnsEmptyList() {
        Job createdJob = integrationTest.createValidJob();
        jobRepository.remove(createdJob);
        Node rootNode = new RSQLParser().parse("active==true;id==" + createdJob.getId());
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.isEmpty());
    }

    @Test
    public void givenJobTitleMatchingWithDoubleWildcardThenFindsIt() {
        Job createdJob = integrationTest.createValidJobWith("Here java is present");
        Node rootNode = new RSQLParser().parse("title=='*java*'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJob.getId().equals(job.getId())));
    }

    @Test
    public void givenJobTitleMatchingWithSingleWildcardThenFindsIt() {
        Job createdJob = integrationTest.createValidJobWith("Junior dev java");
        Node rootNode = new RSQLParser().parse("title=='*java'");
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertTrue(foundJobs.stream()
                .anyMatch(job -> createdJob.getId().equals(job.getId())));
    }

    @Test
    public void givenInactiveJobIdThenFindsIt() {
        Job createdJob = integrationTest.createValidJob();
        jobRepository.remove(createdJob);
        Node rootNode = new RSQLParser().parse("id==" + createdJob.getId());
        Specification<Job> rsqlBasedSpecification = rootNode.accept(new ManagedEntityRSQLVisitor<>());
        List<Job> foundJobs = jobRepository.findBySpecification(rsqlBasedSpecification);
        assertEquals(1, foundJobs.size());
        assertEquals(createdJob.getId(), foundJobs.get(0).getId());
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
