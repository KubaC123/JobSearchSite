package getjobin.it.portal.elasticservice.client.management;

import api.IndexMappingDTO;
import getjobin.it.portal.elasticservice.client.management.control.ElasticManagementMapper;
import getjobin.it.portal.elasticservice.client.management.control.ElasticManagementService;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = ElasticManagementResource.MAIN_PATH)
public class ElasticManagementResource {

    static final String MAIN_PATH = "management";
    private static final String INDEX_PATH = "index";
    private static final String INDEX_NAME_PARAM = "name";

    private ElasticManagementService elasticManagementService;

    private ElasticManagementMapper elasticManagementMapper;

    @Autowired
    public ElasticManagementResource(ElasticManagementService elasticManagementService, ElasticManagementMapper elasticManagementMapper) {
        this.elasticManagementService = elasticManagementService;
        this.elasticManagementMapper = elasticManagementMapper;
    }

    @RequestMapping(method = RequestMethod.GET, value = INDEX_PATH)
    public List<IndexMappingDTO> getMappings(@RequestParam(INDEX_NAME_PARAM) String commaSeparatedIndexesNames) {
        Map<String, MappingMetaData> mappings = elasticManagementService.getMappings(getIndexesAsList(commaSeparatedIndexesNames));
        return elasticManagementMapper.toIndexMappingsDTO(mappings);
    }

    private List<String> getIndexesAsList(String commaSeparatedIndexesNames) {
        return Arrays.stream(commaSeparatedIndexesNames.split(","))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST, value = INDEX_PATH)
    public ResponseEntity createIndex(@RequestParam(INDEX_NAME_PARAM) String indexName) {
        elasticManagementService.createIndex(indexName);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = INDEX_PATH)
    public ResponseEntity deleteIndex(@RequestParam(INDEX_NAME_PARAM) String indexName) {
        elasticManagementService.deleteIndex(indexName);
        return new ResponseEntity(HttpStatus.OK);
    }
}
