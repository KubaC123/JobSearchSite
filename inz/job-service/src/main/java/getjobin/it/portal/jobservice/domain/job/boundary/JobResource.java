package getjobin.it.portal.jobservice.domain.job.boundary;

import getjobin.it.portal.elasticservice.api.DocumentDto;
import getjobin.it.portal.elasticservice.api.SearchRequestDto;
import getjobin.it.portal.jobservice.api.JobDto;
import getjobin.it.portal.jobservice.api.JobSimpleDto;
import getjobin.it.portal.jobservice.api.ResourceDto;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.control.JobServiceTestData;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.JobWithRelatedObjects;
import getjobin.it.portal.jobservice.infrastructure.config.security.IsAdmin;
import getjobin.it.portal.jobservice.infrastructure.config.security.IsRecruiter;
import getjobin.it.portal.jobservice.infrastructure.exception.JobServicePreconditions;
import getjobin.it.portal.jobservice.infrastructure.rest.IdsParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = JobResource.JOB_PATH)
@Slf4j
public class JobResource {

    public static final String JOB_PATH = "api/job";

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobServiceTestData jobServiceTestData;

    @IsAdmin
    @RequestMapping(method = RequestMethod.GET, value = "all")
    public List<JobDto> findAll() {
        List<Job> foundJobs = jobService.findAll();
        List<JobWithRelatedObjects> jobsWithRelatedObjects = jobService.getJobsWithRelatedObjects(foundJobs);
        return jobMapper.toDtos(jobsWithRelatedObjects);
    }

    @RequestMapping(method = RequestMethod.GET, value = "scroll")
    public List<JobDto> scrollJobs(@RequestParam("startRow") Integer startRow,
                                   @RequestParam("rowsCount") Integer rowsCount) {
        List<Job> foundJobs = jobService.scrollJobs(startRow, rowsCount);
        List<JobWithRelatedObjects> jobsWithRelatedObjects = jobService.getJobsWithRelatedObjects(foundJobs);
        return jobMapper.toDtos(jobsWithRelatedObjects);
    }

    @RequestMapping(method = RequestMethod.GET, value = IdsParam.IDS_PATH)
    public List<JobDto> searchByIds(@PathVariable(IdsParam.IDS) IdsParam ids) {
        List<Job> foundJobs = jobService.findByIds(ids.asList());
        List<JobWithRelatedObjects> jobsWithRelatedObjects = jobService.getJobsWithRelatedObjects(foundJobs);
        return jobMapper.toDtos(jobsWithRelatedObjects);
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.GET, value = "query")
    public List<JobDto> searchByRsql(@RequestParam("rsql") String rsql) {
        List<Job> foundJobs = jobService.findByRsqlCondition(rsql);
        List<JobWithRelatedObjects> jobsWithRelatedObjects = jobService.getJobsWithRelatedObjects(foundJobs);
        return jobMapper.toDtos(jobsWithRelatedObjects);
    }

    @RequestMapping(method = RequestMethod.POST, value = "search")
    public List<JobDto> searchByTextOnAttributesElasticSearch(@RequestBody SearchRequestDto jobSearchDto) {
        List<JobWithRelatedObjects> jobsWithRelatedObjects = jobService.searchByTextOnAttributesElasticSearch(jobSearchDto);
        return jobMapper.toDtos(jobsWithRelatedObjects);
    }

    @RequestMapping(method = RequestMethod.GET, value = "search")
    public List<JobSimpleDto> searchByTextElasticSearch(@RequestParam("searchText") String searchText) {
        List<DocumentDto> jobs = jobService.searchByTextElasticSearch(searchText);
        return jobMapper.toSimpleDtos(jobs);
    }

    @RequestMapping(method = RequestMethod.GET, value = "search/sql")
    public List<JobDto> searchByTextSql(@RequestParam("searchText") String searchText) {
        List<JobWithRelatedObjects> jobsWithRelatedObjects = jobService.searchByTextSql(searchText);
        return jobMapper.toDtos(jobsWithRelatedObjects);
    }

    @RequestMapping(method = RequestMethod.POST, value = "bulk")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResourceDto createJobs(@RequestParam("n") Integer n) {
        Instant start = Instant.now();
        jobServiceTestData.createNJobsAsync();
        Instant end = Instant.now();
        log.info("[TEST CREATE] Created {} jobs in {} minutes", n, Duration.between(start, end).toMinutes());
        return ResourceDto.builder()
                .objectType(Job.JOB_TYPE)
                .build();
    }

    @IsAdmin @IsRecruiter
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResourceDto createJob(@RequestBody JobDto jobDto) {
        Job job = jobMapper.toEntity(jobDto);
        Long createdJobId = jobService.create(job);
        return buildResourceDTO(createdJobId);
    }

    private ResourceDto buildResourceDTO(Long jobId) {
        return ResourceDto.builder()
                .objectType(Job.JOB_TYPE)
                .objectId(jobId)
                .resourceURI(ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/" + jobId)
                        .build()
                        .toUri())
                .build();
    }

    @IsAdmin @IsRecruiter
    @RequestMapping(method = RequestMethod.PUT)
    public ResourceDto updateJob(@RequestBody JobDto jobDTO) {
        JobServicePreconditions.checkArgument(jobDTO.getId() != null, "Job id must be specified in Dto in order to update it");
        Job existingJob = jobService.getById(jobDTO.getId());
        Job updatedJob = jobMapper.updateExistingJobOffer(existingJob, jobDTO);
        jobService.update(updatedJob);
        return buildResourceDTO(jobDTO.getId());
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.DELETE, value = IdsParam.IDS_PATH)
    public void deleteJob(@PathVariable(IdsParam.IDS) IdsParam ids) {
        jobService.findByIds(ids.asList())
                .forEach(jobService::remove);
    }
}
