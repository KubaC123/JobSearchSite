package getjobin.it.portal.jobservice.domain.job.boundary;

import getjobin.it.portal.jobservice.api.JobDto;
import getjobin.it.portal.jobservice.api.ResourceDto;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.infrastructure.exception.JobServicePreconditions;
import getjobin.it.portal.jobservice.infrastructure.util.IdsParam;
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

import javax.websocket.server.PathParam;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = JobResource.JOB_PATH)
@Slf4j
public class JobResource {

    public static final String JOB_PATH = "job";

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobService jobService;

    @RequestMapping(method = RequestMethod.GET, value = IdsParam.IDS_PATH)
    public List<JobDto> browseJobs(@PathVariable(IdsParam.IDS) IdsParam ids) {
        List<Job> foundJobs = jobService.findByIds(ids.asList());
        return jobMapper.toDtos(foundJobs);
    }

    @RequestMapping(method = RequestMethod.GET, value = "search")
    public List<JobDto> searchByRsql(@RequestParam("rsql") String rsql) {
        List<Job> foundJobs = jobService.findByRsqlCondition(rsql);
        return jobMapper.toDtos(foundJobs);
    }

    @RequestMapping(method = RequestMethod.GET, value = "search/fullText")
    public List<JobDto> searchByTextUsingElasticSearch(@RequestParam("searchText") String searchText) {
        Instant start = Instant.now();
        List<Job> foundJobs = jobService.searchByTextUsingElasticSearch(searchText);
        Instant end = Instant.now();
        log.info("[FULL TEXT SEARCH] ES search took {} ms.", Duration.between(start, end).toMillis());
        return jobMapper.toDtos(foundJobs);
    }

    @RequestMapping(method = RequestMethod.GET, value = "search/fullText/sql")
    public List<JobDto> searchByTextUsingSql(@RequestParam("searchText") String searchText) {
        Instant start = Instant.now();
        List<Job> foundJobs = jobService.searchByTextUsingSql(searchText);
        Instant end = Instant.now();
        log.info("[FULL TEXT SEARCH] SQL search took {} ms.", Duration.between(start, end).toMillis());
        return jobMapper.toDtos(foundJobs);
    }

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

    @RequestMapping(method = RequestMethod.PUT)
    public ResourceDto updateJob(@RequestBody JobDto jobDTO) {
        JobServicePreconditions.checkArgument(jobDTO.getId() != null, "Job id must be specified in DTO order to update it");
        Job existingJob = jobService.getById(jobDTO.getId());
        Job updatedJob = jobMapper.updateExistingJobOffer(existingJob, jobDTO);
        jobService.update(updatedJob);
        return buildResourceDTO(jobDTO.getId());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = IdsParam.IDS)
    public void deleteJob(@PathParam(IdsParam.IDS) IdsParam ids) {
        jobService.findByIds(ids.asList())
                .forEach(jobService::remove);
    }
}
