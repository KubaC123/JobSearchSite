package getjobin.it.portal.jobservice.domain.search;

import getjobin.it.portal.jobservice.api.client.IndexMappingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = IndexationResource.MAIN_PATH)
public class IndexationResource {

    static final String MAIN_PATH = "indexation";
    private static final String MAPPING_PATH = "mapping";
    private static final String COMPANY_MAPPING_PATH = MAPPING_PATH + "/company";

    private ElasticSearchMappingProvider mappingProvider;

    private IndexationService indexationService;

    @Autowired
    public IndexationResource(ElasticSearchMappingProvider mappingProvider, IndexationService indexationService) {
        this.mappingProvider = mappingProvider;
        this.indexationService = indexationService;
    }

    @RequestMapping(method = RequestMethod.GET, value = COMPANY_MAPPING_PATH)
    public IndexMappingDTO getCompanyMapping() {
        return IndexMappingDTO.builder()
                .indexName("company")
                .mapping(mappingProvider.buildCompanyMapping())
                .build();
    }

    @RequestMapping(method = RequestMethod.PUT, value = COMPANY_MAPPING_PATH)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void putCompanyMapping() {
        indexationService.sendMappingOnTopic(IndexMappingDTO.builder()
                .indexName("company")
                .mapping(mappingProvider.buildCompanyMapping())
                .build());
    }

}
