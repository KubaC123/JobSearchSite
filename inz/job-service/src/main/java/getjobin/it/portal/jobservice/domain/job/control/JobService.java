package getjobin.it.portal.jobservice.domain.job.control;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.ast.Node;
import getjobin.it.portal.elasticservice.api.FoundDocumentDto;
import getjobin.it.portal.jobservice.client.ElasticServiceClient;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.indexation.boundary.IndexationMapper;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.JobLocationRelation;
import getjobin.it.portal.jobservice.domain.job.entity.JobTechStackRelation;
import getjobin.it.portal.jobservice.domain.job.entity.JobWithRelatedObjects;
import getjobin.it.portal.jobservice.domain.location.control.LocationService;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.domain.search.control.GenericRSQLSpecification;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.infrastructure.exception.JobServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private Validator validator;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private TechStackService techStackService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private IndexationMapper indexationMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ElasticServiceClient elasticServiceClient;

    @Value("${getjobin.it.portal.job.sql.fulltext.attributes}")
    private String sqlFullTextSearchCommaSeparatedAttributes;

    @Value("${getjobin.it.portal.job.elastic.fulltext.attributes}")
    private String elasticFullTextSearchCommaSeparatedAttributes;

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public List<JobWithRelatedObjects> getJobsWithRelatedObjects(List<Job> jobs) {
        return jobs.stream()
                .map(this::getJobWithRelatedObjects)
                .collect(Collectors.toList());
    }

    private JobWithRelatedObjects getJobWithRelatedObjects(Job job) {
        return JobWithRelatedObjects.builder()
                .job(job)
                .techStacksWithExperienceLevel(getTechStacks(job))
                .locationsWithRemote(getLocations(job))
                .build();
    }

    private List<Pair<TechStack, Integer>> getTechStacks(Job job) {
        return job.getTechStackRelations().map(this::queryTechStacks)
                .orElseGet(ArrayList::new);
    }

    private List<Pair<TechStack, Integer>> queryTechStacks(List<JobTechStackRelation> techStackRelations) {
        return techStackRelations.stream()
                .map(relation -> Pair.of(techStackService.getById(relation.getTechStackId()), relation.getExperienceLevel()))
                .collect(Collectors.toList());
    }

    private List<Pair<Location, Boolean>> getLocations(Job job) {
        return job.getLocationRelations().map(this::queryLocations)
                .orElseGet(ArrayList::new);
    }

    private List<Pair<Location, Boolean>> queryLocations(List<JobLocationRelation> locationRelations) {
        return locationRelations.stream()
                .map(relation -> Pair.of(locationService.getById(relation.getLocationId()), relation.getRemote()))
                .collect(Collectors.toList());
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
                .getDocuments()
                .stream()
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
        sendIndexationEvent(createdJobId, OperationType.CREATE);
        return createdJobId;
    }

    private void sendIndexationEvent(Long jobId, OperationType operationType) {
        eventPublisher.publishEvent(indexationMapper.toDocumentEventDto(getById(jobId), operationType));
    }

    private void validate(Job job) {
        Set<ConstraintViolation<Job>> violations = validator.validate(job);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Long update(Job job) {
        validate(job);
        Long updatedJobId = jobRepository.update(job);
        sendIndexationEvent(updatedJobId, OperationType.UPDATE);
        return updatedJobId;
    }

    public void remove(Job job) {
        jobRepository.remove(job);
    }
}
