package getjobin.it.portal.jobservice.domain.joboffer.boundary;

import getjobin.it.portal.jobservice.api.JobOfferDTO;
import getjobin.it.portal.jobservice.api.ResourceDTO;
import getjobin.it.portal.jobservice.domain.joboffer.control.JobOfferMapper;
import getjobin.it.portal.jobservice.domain.joboffer.control.JobOfferService;
import getjobin.it.portal.jobservice.domain.joboffer.entity.JobOffer;
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
@RequestMapping(value = JobOfferResource.MAIN_PATH)
public class JobOfferResource {

    static final String MAIN_PATH = "jobOffer";

    private JobOfferMapper jobOfferMapper;
    private JobOfferService jobOfferService;

    @Autowired
    public JobOfferResource(JobOfferMapper jobOfferMapper, JobOfferService jobOfferService) {
        this.jobOfferMapper = jobOfferMapper;
        this.jobOfferService = jobOfferService;
    }

    @RequestMapping(method = RequestMethod.GET, value = IdsParam.IDS_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public List<JobOfferDTO> browseJobOffers(@PathVariable(IdsParam.IDS) IdsParam ids) {
        return jobOfferService.findByIds(ids.asList()).stream()
                .map(jobOfferMapper::toDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResourceDTO createJobOffer(@RequestBody JobOfferDTO jobOfferDTO) {
        JobOffer jobOffer = jobOfferMapper.toEntity(jobOfferDTO);
        Long createdJobOfferId = jobOfferService.createJobOffer(jobOffer);
        return buildResourceDTO(createdJobOfferId);
    }

    private ResourceDTO buildResourceDTO(Long jobOfferId) {
        return ResourceDTO.builder()
                .objectType(JobOffer.JOB_OFFER_TYPE)
                .objectId(jobOfferId)
                .resourceURI(ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/" + jobOfferId)
                        .build()
                        .toUri())
                .build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public ResourceDTO updateJobOffer(@RequestBody JobOfferDTO jobOfferDTO) {
        JobServicePreconditions.checkArgument(jobOfferDTO.getId() != null, "Job offer id must be specified in DTO order to update it");
        JobOffer existingJobOffer = jobOfferService.getById(jobOfferDTO.getId());
        JobOffer updatedJobOffer = jobOfferMapper.updateExistingJobOffer(existingJobOffer, jobOfferDTO);
        jobOfferService.updateJobOffer(updatedJobOffer);
        return buildResourceDTO(jobOfferDTO.getId());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = IdsParam.IDS)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteJobOffers(@PathParam(IdsParam.IDS) IdsParam ids) {
        jobOfferService.findByIds(ids.asList())
                .forEach(jobOfferService::removeJobOffer);
    }
}
