package getjobin.it.portal.jobservice.domain.job.control;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.ast.Node;
import getjobin.it.portal.elasticservice.api.FoundDocumentDto;
import getjobin.it.portal.jobservice.client.ElasticServiceClient;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.indexation.control.IndexationService;
import getjobin.it.portal.jobservice.domain.job.boundary.OperationType;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.search.control.GenericRSQLSpecification;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.infrastructure.exception.JobServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobService {

    private Validator validator;
    private JobRepository jobRepository;
    private IndexationService indexationService;
    private ElasticServiceClient elasticServiceClient;

    @Value("${getjobin.it.portal.job.sql.fulltext.attributes}")
    private String sqlFullTextSearchCommaSeparatedAttributes;

    @Value("${getjobin.it.portal.job.elastic.fulltext.attributes}")
    private String elasticFullTextSearchCommaSeparatedAttributes;

    @Autowired
    public JobService(Validator validator, JobRepository jobRepository,
                      ElasticServiceClient elasticServiceClient, IndexationService indexationService) {
        this.validator = validator;
        this.jobRepository = jobRepository;
        this.elasticServiceClient = elasticServiceClient;
        this.indexationService = indexationService;
    }

    public Optional<Job> findById(Long jobId) {
        return jobRepository.findById(jobId);
    }

    public List<Job> findByIds(List<Long> jobIds) {
        return jobRepository.findByIds(jobIds);
    }

    public Job getById(Long jobId) {
        return jobRepository.getById(jobId);
    }

    public List<Job> findByCompany(Company company) {
        return jobRepository.findByCompany(company);
    }

    public List<Job> findByTechnology(Technology technology) {
        return jobRepository.findByTechnology(technology);
    }

    public List<Job> findByRsqlCondition(String rsqlCondition) {
        Node node = tryParseRSQLCondition(rsqlCondition);
        return jobRepository.findByRSQLNode(node);
    }

    private Node tryParseRSQLCondition(String rsqlCondition) {
        try {
            return new RSQLParser().parse(rsqlCondition);
        } catch (RSQLParserException exception) {
            throw new JobServiceException("Invalid rsql syntax. Please refer to swagger documentation for valid operators.");
        }
    }

    public List<Job> searchByTextUsingElasticSearch(String searchText) {
        return elasticServiceClient.fullTextSearch("job", searchText, elasticFullTextSearchCommaSeparatedAttributes)
                .getDocuments().stream()
                .map(FoundDocumentDto::getObjectId)
                .map(jobRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Job> searchByTextUsingSql(String searchText) {
        String rsqlCondition = Arrays.stream(sqlFullTextSearchCommaSeparatedAttributes.split(","))
                .map(attribute -> attribute + GenericRSQLSpecification.RSQL_EQUAL_TO + searchText)
                .collect(Collectors.joining(GenericRSQLSpecification.RSQL_LOGICAL_OR));
        return findByRsqlCondition(rsqlCondition);
    }

    public Long create(Job job) {
        validate(job);
        Long createdJobId = jobRepository.save(job);
        indexJob(createdJobId, OperationType.CREATE);
        return createdJobId;
    }

    private void indexJob(Long jobId, OperationType operationType) {
        indexationService.indexObjectsAsync(Collections.singletonList(jobId), Job.JOB_TYPE, operationType);
    }

    private void validate(Job job) {
        Set<ConstraintViolation<Job>>  violations = validator.validate(job);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Long update(Job job) {
        validate(job);
        Long updatedJobId = jobRepository.update(job);
        indexJob(updatedJobId, OperationType.UPDATE);
        return updatedJobId;
    }

    public void remove(Job job) {
        jobRepository.remove(job);
    }
}
