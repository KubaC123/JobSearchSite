package getjobin.it.portal.jobservice.domain.joboffer.boundary;

import getjobin.it.portal.jobservice.api.JobOfferDTO;
import getjobin.it.portal.jobservice.api.ResourceDTO;
import getjobin.it.portal.jobservice.domain.joboffer.control.JobOfferMapper;
import getjobin.it.portal.jobservice.domain.joboffer.control.JobOfferService;
import getjobin.it.portal.jobservice.domain.joboffer.control.JobTechStackRelationMapper;
import getjobin.it.portal.jobservice.domain.joboffer.entity.JobOffer;
import getjobin.it.portal.jobservice.domain.joboffer.entity.JobTechStackRelation;
import getjobin.it.portal.jobservice.infrastructure.IdsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(value = JobOfferResource.MAIN_PATH)
public class JobOfferResource {

    static final String MAIN_PATH = "jobOffer";

    private JobOfferMapper jobOfferMapper;
    private JobOfferService jobOfferService;
    private JobTechStackRelationMapper jobTechStackRelationMapper;

    @Autowired
    public JobOfferResource(JobOfferMapper jobOfferMapper, JobOfferService jobOfferService,
                            JobTechStackRelationMapper jobTechStackRelationMapper) {
        this.jobOfferMapper = jobOfferMapper;
        this.jobOfferService = jobOfferService;
        this.jobTechStackRelationMapper = jobTechStackRelationMapper;
    }

    @RequestMapping(method = RequestMethod.GET, value = IdsParam.IDS_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public List<JobOfferDTO> browseJobOffers(@PathVariable(IdsParam.IDS) IdsParam ids) {
        // todo
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResourceDTO createJobOffer(@RequestBody JobOfferDTO jobOfferDTO) {
        JobOffer jobOffer = jobOfferMapper.toEntity(jobOfferDTO);
        Long createdJobOfferId = jobOfferService.createJobOffer(jobOffer, jobOfferDTO.getTechStacks());
        List<JobTechStackRelation> jobTechStackRelations = jobTechStackRelationMapper.toEntities(createdJobOfferId, jobOfferDTO.getTechStacks());
        jobOfferService.createJobOfferTechStackRelations(jobTechStackRelations);
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
}
