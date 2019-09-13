package getjobin.it.portal.jobservice.domain.joboffer.boundary;

import getjobin.it.portal.jobservice.api.JobDTO;
import getjobin.it.portal.jobservice.api.ResourceDTO;
import getjobin.it.portal.jobservice.domain.joboffer.control.JobMapper;
import getjobin.it.portal.jobservice.domain.joboffer.control.JobService;
import getjobin.it.portal.jobservice.domain.joboffer.entity.Job;
import getjobin.it.portal.jobservice.infrastructure.IdsParam;
import getjobin.it.portal.jobservice.infrastructure.exceptions.JobServicePreconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = JobResource.MAIN_PATH)
public class JobResource {

    static final String MAIN_PATH = "job";

    private JobMapper jobMapper;
    private JobService jobService;

    @Autowired
    public JobResource(JobMapper jobMapper, JobService jobService) {
        this.jobMapper = jobMapper;
        this.jobService = jobService;
    }

    @RequestMapping(method = RequestMethod.GET, value = IdsParam.IDS_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public List<JobDTO> browseJobs(@PathVariable(IdsParam.IDS) IdsParam ids) {
        return jobService.findByIds(ids.asList()).stream()
                .map(jobMapper::toDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResourceDTO createJob(@RequestBody JobDTO jobDTO) {
        Job job = jobMapper.toEntity(jobDTO);
        Long createdJobId = jobService.createJob(job);
        return buildResourceDTO(createdJobId);
    }

    private ResourceDTO buildResourceDTO(Long jobId) {
        return ResourceDTO.builder()
                .objectType(Job.JOB_OFFER_TYPE)
                .objectId(jobId)
                .resourceURI(ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/" + jobId)
                        .build()
                        .toUri())
                .build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public ResourceDTO updateJob(@RequestBody JobDTO jobDTO) {
        JobServicePreconditions.checkArgument(jobDTO.getId() != null, "Job id must be specified in DTO order to update it");
        Job existingJob = jobService.getById(jobDTO.getId());
        Job updatedJob = jobMapper.updateExistingJobOffer(existingJob, jobDTO);
        jobService.updateJob(updatedJob);
        return buildResourceDTO(jobDTO.getId());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = IdsParam.IDS)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteJob(@PathParam(IdsParam.IDS) IdsParam ids) {
        jobService.findByIds(ids.asList())
                .forEach(jobService::removeJob);
    }
}
