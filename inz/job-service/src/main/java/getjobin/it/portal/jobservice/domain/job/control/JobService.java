package getjobin.it.portal.jobservice.domain.job.control;

import com.google.common.collect.ImmutableMap;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.ast.Node;
import getjobin.it.portal.elasticservice.api.DocumentDto;
import getjobin.it.portal.elasticservice.api.DocumentEventDto;
import getjobin.it.portal.elasticservice.api.SearchRequestDto;
import getjobin.it.portal.elasticservice.api.SearchResultDto;
import getjobin.it.portal.jobservice.client.ElasticServiceClient;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.job.boundary.JobMapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RefreshScope
@Slf4j
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
    private JobMapper jobMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ElasticServiceClient elasticServiceClient;

    Map<String, Float> esFullTextFieldsWithBoost = Stream.of(new Object[][] {
            { "title", 1.5f },
            { "companyName", 0.5f },
            { "description", 2f },
            { "technologyName", 3f },
            { "projectDescription", 0.5f },
            { "techStacks", 3f },
            { "cities", 2.0f },
            { "countries", 1.0f },
            { "streets", 1.0f },
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Float) data[1]));

    @Value("${getjobin.it.portal.job.sql.fulltext.attributes}")
    private String sqlFullTextSearchCommaSeparatedAttributes;

    @Value("${getjobin.it.portal.job.elastic.fulltext.attributes}")
    private String elasticFullTextSearchCommaSeparatedAttributes;

    public List<DocumentEventDto> getDocumentEvents(Integer startRow, Integer endRow) {
        List<Job> jobs = jobRepository.queryPartition(startRow, endRow);
        return toDocumentEvents(jobs);
    }

    public List<DocumentEventDto> getDocumentEvents(List<Long> ids) {
        List<Job> jobs = jobRepository.findByIds(ids);
        return toDocumentEvents(jobs);
    }

    private List<DocumentEventDto> toDocumentEvents(List<Job> jobs) {
        return jobs.stream()
                .map(this::getJobWithRelatedObjects)
                .map(jobMapper::toDocumentEventDto)
                .collect(Collectors.toList());
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public List<Job> scrollJobs(Integer startRow, Integer rowsCount) {
        return jobRepository.queryPartition(startRow, startRow + rowsCount);
    }

    public List<JobWithRelatedObjects> getJobsWithRelatedObjects(List<Job> jobs) {
        return jobs.stream()
                .map(this::getJobWithRelatedObjects)
                .collect(Collectors.toList());
    }

    public JobWithRelatedObjects getJobWithRelatedObjects(Job job) {
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

    public List<JobWithRelatedObjects> searchByTextOnAttributesElasticSearch(SearchRequestDto searchRequest) {
        Instant start = Instant.now();
        SearchResultDto searchResult = elasticServiceClient.fullTextSearch(searchRequest);
        List<Long> foundJobIds = getJobIds(searchResult);
        Instant end = Instant.now();
        log.info("[FULL TEXT SEARCH] ES search took {} ms", Duration.between(start, end).toMillis());
        return getJobsWithRelatedObjects(findByIds(foundJobIds));
    }

    public List<DocumentDto> searchByTextElasticSearch(String searchText) {
        Instant start = Instant.now();
        SearchResultDto searchResult = elasticServiceClient.fullTextSearch(buildSearchRequest(searchText));
        Instant end = Instant.now();
        log.info("[FULL TEXT SEARCH] ES search took {} ms", Duration.between(start, end).toMillis());
        return searchResult.getDocuments();
    }

    private SearchRequestDto buildSearchRequest(String searchText) {
        return SearchRequestDto.builder()
                .index("job")
                .searchText(searchText)
                .fieldsWithBoost(esFullTextFieldsWithBoost)
                .build();
    }

    private List<Long> getJobIds(SearchResultDto searchResultDto) {
        return searchResultDto
                .getDocuments()
                .stream()
                .map(DocumentDto::getObjectId)
                .collect(Collectors.toList());
    }

    public List<JobWithRelatedObjects> searchByTextSql(String searchText) {
        Instant start = Instant.now();
        List<String> words = getWords(searchText);

        Optional<String> locationRelatedText = getLocationRelatedText(words);
        locationRelatedText.ifPresent(words::remove);

        String rsqlCondition = getRsqlCondition(words);
        List<Job> foundJobs = findByRsqlCondition(rsqlCondition);

        List<JobWithRelatedObjects> jobsWithRelatedObjects = getJobsWithRelatedObjects(foundJobs);

        if(locationRelatedText.isPresent()) {
            jobsWithRelatedObjects = filterJobsBasedOnLocation(locationRelatedText.get(), jobsWithRelatedObjects);
        }

        Instant end = Instant.now();
        log.info("[FULL TEXT SEARCH] SQL search took {} ms", Duration.between(start, end).toMillis());
        return jobsWithRelatedObjects;
    }

    private List<JobWithRelatedObjects> filterJobsBasedOnLocation(String locationRelatedText, List<JobWithRelatedObjects> jobsWithRelatedObjects) {
        return jobsWithRelatedObjects.stream()
                .filter(job -> filterBasedOnLocation(job, locationRelatedText))
                .collect(Collectors.toList());
    }

    private List<String> getWords(String searchText) {
        return Arrays.stream(searchText.split(" "))
                .collect(Collectors.toList());
    }

    private Optional<String> getLocationRelatedText(List<String> words) {
        return words.stream()
                .filter(locationService::isSearchTextLocationRelated)
                .findFirst();
    }

    private String getRsqlCondition(List<String> words) {
        return Arrays.stream(sqlFullTextSearchCommaSeparatedAttributes.split(","))
                .map(attribute -> toRsqlEqual(attribute, words))
                .collect(Collectors.joining(GenericRSQLSpecification.RSQL_LOGICAL_OR));
    }

    private String toRsqlEqual(String attribute, List<String> words) {
        return attribute + GenericRSQLSpecification.RSQL_EQUAL_TO + "'*" + String.join(" ", words) + "*'";
    }

    private boolean filterBasedOnLocation(JobWithRelatedObjects job, String searchText) {
        return job.getLocationsWithRemote().stream()
                .map(Pair::getFirst)
                .anyMatch(location -> locationService.doesSearchTextOccur(location, searchText));
    }


    public Long create(Job job) {
        validate(job);
        Long createdJobId = jobRepository.save(job);
        sendIndexationEvent(createdJobId);
        return createdJobId;
    }

    private void sendIndexationEvent(Long jobId) {
        JobWithRelatedObjects jobWithRelatedObjects = getJobWithRelatedObjects(getById(jobId));
        DocumentEventDto event = jobMapper.toDocumentEventDto(jobWithRelatedObjects);
        eventPublisher.publishEvent(event);
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
        sendIndexationEvent(updatedJobId);
        return updatedJobId;
    }

    public void remove(Job job) {
        jobRepository.remove(job);
    }
}
