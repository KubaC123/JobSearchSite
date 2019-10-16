package getjobin.it.portal.jobservice.domain.indexation.boundary;

import getjobin.it.portal.elasticservice.api.MappingEventDto;
import getjobin.it.portal.jobservice.domain.indexation.control.IndexationService;
import getjobin.it.portal.jobservice.domain.indexation.control.MappingService;
import getjobin.it.portal.jobservice.domain.job.boundary.JobResource;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.infrastructure.config.security.IsAdmin;
import getjobin.it.portal.jobservice.infrastructure.rest.IdsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = IndexationResource.INDEXATION_PATH)
public class IndexationResource {

    public static final String INDEXATION_PATH = "api/indexation";
    private static final String MAPPING_PATH = "mapping";
    private static final String JOB_PATH = "job";
    private static final String COMPANY_PATH = "company";
    private static final String COMPANY_MAPPING_PATH = MAPPING_PATH + "/" + COMPANY_PATH;
    private static final String JOB_MAPPING_PATH = MAPPING_PATH + "/" + JOB_PATH;

    @Autowired
    private ElasticSearchMappingProvider mappingProvider;

    @Autowired
    private IndexationService indexationService;

    @Autowired
    private MappingService mappingService;

    @RequestMapping(method = RequestMethod.GET, value = COMPANY_MAPPING_PATH)
    public MappingEventDto getCompanyMapping() {
        return mappingProvider.buildCompanyIndexMapping();
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.PUT, value = COMPANY_MAPPING_PATH)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void sendCompanyMapping() {
        mappingService.sendMappingOnTopic(mappingProvider.buildCompanyIndexMapping());
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.GET, value = JOB_MAPPING_PATH)
    public MappingEventDto getJobMapping() {
        return mappingProvider.buildJobIndexMapping();
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.PUT, value = JOB_MAPPING_PATH)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void sendJobMapping() {
        mappingService.sendMappingOnTopic(mappingProvider.buildJobIndexMapping());
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.POST, value = JOB_PATH)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void performAsynchronousJobIndexation(@RequestParam("ids") IdsParam ids) {
        List<Long> specifiedIds = ids.asList();
        if(specifiedIds.isEmpty()) {
            indexationService.indexAllObjectsAsync(Job.class);
        } else {
            indexationService.indexGivenObjectsAsync(specifiedIds, Job.class);
        }
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.POST, value = JOB_PATH + "/full")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void performAsynchronousAllJobIndexation() {
        indexationService.indexAllObjectsAsync(Job.class);
    }
}
