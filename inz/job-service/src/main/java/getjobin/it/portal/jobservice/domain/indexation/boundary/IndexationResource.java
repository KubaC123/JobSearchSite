package getjobin.it.portal.jobservice.domain.indexation.boundary;

import getjobin.it.portal.jobservice.api.client.IndexMappingDTO;
import getjobin.it.portal.jobservice.domain.company.boundary.CompanyResource;
import getjobin.it.portal.jobservice.domain.job.boundary.OperationType;
import getjobin.it.portal.jobservice.domain.job.boundary.JobResource;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.indexation.control.IndexationService;
import getjobin.it.portal.jobservice.domain.indexation.control.MappingService;
import getjobin.it.portal.jobservice.infrastructure.util.IdsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = IndexationResource.MAIN_PATH)
public class IndexationResource {

    static final String MAIN_PATH = "indexation";
    private static final String MAPPING_PATH = "mapping";
    private static final String COMPANY_MAPPING_PATH = MAPPING_PATH + "/" + CompanyResource.COMPANY_PATH;
    private static final String JOB_MAPPING_PATH = MAPPING_PATH + "/" + JobResource.JOB_PATH;

    private ElasticSearchMappingProvider mappingProvider;

    private IndexationService indexationService;

    private MappingService mappingService;

    @Autowired
    public IndexationResource(ElasticSearchMappingProvider mappingProvider, IndexationService indexationService, MappingService mappingService) {
        this.mappingProvider = mappingProvider;
        this.indexationService = indexationService;
        this.mappingService = mappingService;
    }

    @RequestMapping(method = RequestMethod.GET, value = COMPANY_MAPPING_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public IndexMappingDTO getCompanyMapping() {
        return mappingProvider.buildCompanyIndexMapping();
    }

    @RequestMapping(method = RequestMethod.PUT, value = COMPANY_MAPPING_PATH)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void sendCompanyMapping() {
        mappingService.sendMappingOnTopic(mappingProvider.buildCompanyIndexMapping());
    }

    @RequestMapping(method = RequestMethod.GET, value = JOB_MAPPING_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public IndexMappingDTO getJobMapping() {
        return mappingProvider.buildJobIndexMapping();
    }

    @RequestMapping(method = RequestMethod.PUT, value = JOB_MAPPING_PATH)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void sendJobMapping() {
        mappingService.sendMappingOnTopic(mappingProvider.buildJobIndexMapping());
    }

    @RequestMapping(method = RequestMethod.POST, value = JobResource.JOB_PATH)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void performAsynchronousJobIndexation(@PathVariable(IdsParam.IDS) IdsParam ids) {
        indexationService.indexObjectsAsync(ids.asList(), Job.JOB_TYPE, OperationType.UPDATE);
    }
}
