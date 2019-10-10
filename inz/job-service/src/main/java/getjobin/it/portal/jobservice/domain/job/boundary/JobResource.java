package getjobin.it.portal.jobservice.domain.job.boundary;

import getjobin.it.portal.jobservice.api.JobDto;
import getjobin.it.portal.jobservice.api.ResourceDto;
import getjobin.it.portal.jobservice.domain.job.control.JobService;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.infrastructure.exception.JobServicePreconditions;
import getjobin.it.portal.jobservice.infrastructure.util.IdsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = JobResource.JOB_PATH)
public class JobResource {

    public static final String JOB_PATH = "job";

    private JobMapper jobMapper;
    private JobService jobService;
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public JobResource(JobMapper jobMapper, JobService jobService, ApplicationEventPublisher applicationEventPublisher) {
        this.jobMapper = jobMapper;
        this.jobService = jobService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @RequestMapping(method = RequestMethod.GET, value = IdsParam.IDS_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public List<JobDto> browseJobs(@PathVariable(IdsParam.IDS) IdsParam ids) {
        return jobService.findByIds(ids.asList()).stream()
                .map(jobMapper::toDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "search")
    @ResponseStatus(value = HttpStatus.OK)
    public List<JobDto> searchByRsql(@RequestParam("rsql") String rsql) {
        return jobService.findByRSQLCondition(rsql).stream()
                .map(jobMapper::toDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "search/fullText")
    @ResponseStatus(value = HttpStatus.OK)
    public List<JobDto> searchByText(@RequestParam("searchText") String searchText) {
        // todo, which fields should have boost ?
        return Collections.emptyList();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResourceDto createJob(@RequestBody JobDto jobDto) {
        Job job = jobMapper.toEntity(jobDto);
        Long createdJobId = jobService.create(job);
        applicationEventPublisher.publishEvent(jobMapper.toEvent(createdJobId, OperationType.CREATE));
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
    @ResponseStatus(value = HttpStatus.OK)
    public ResourceDto updateJob(@RequestBody JobDto jobDTO) {
        JobServicePreconditions.checkArgument(jobDTO.getId() != null, "Job id must be specified in DTO order to update it");
        Job existingJob = jobService.getById(jobDTO.getId());
        Job updatedJob = jobMapper.updateExistingJobOffer(existingJob, jobDTO);
        jobService.update(updatedJob);
        applicationEventPublisher.publishEvent(jobMapper.toEvent(jobDTO.getId(), OperationType.UPDATE));
        return buildResourceDTO(jobDTO.getId());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = IdsParam.IDS)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteJob(@PathParam(IdsParam.IDS) IdsParam ids) {
        jobService.findByIds(ids.asList())
                .forEach(job -> {
                    jobService.remove(job);
                    applicationEventPublisher.publishEvent(jobMapper.toEvent(job.getId(), OperationType.DELETE));
                });
    }
}
