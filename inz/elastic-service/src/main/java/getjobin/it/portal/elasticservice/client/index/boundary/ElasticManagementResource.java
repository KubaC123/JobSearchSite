package getjobin.it.portal.elasticservice.client.index.boundary;

import api.IndexMappingDTO;
import getjobin.it.portal.elasticservice.client.index.control.ElasticManagementMapper;
import getjobin.it.portal.elasticservice.client.index.control.IndexManagementService;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    private IndexManagementService indexManagementService;

    private ElasticManagementMapper elasticManagementMapper;

    @Autowired
    public ElasticManagementResource(IndexManagementService indexManagementService, ElasticManagementMapper elasticManagementMapper) {
        this.indexManagementService = indexManagementService;
        this.elasticManagementMapper = elasticManagementMapper;
    }

    @RequestMapping(method = RequestMethod.GET, value = INDEX_PATH)
    public List<IndexMappingDTO> getMappings(@RequestParam(INDEX_NAME_PARAM) String commaSeparatedIndexesNames) {
        Map<String, MappingMetaData> mappings = indexManagementService.getMappings(getIndexesAsList(commaSeparatedIndexesNames));
        return elasticManagementMapper.toIndexMappingDTOs(mappings);
    }

    private List<String> getIndexesAsList(String commaSeparatedIndexesNames) {
        return Arrays.stream(commaSeparatedIndexesNames.split(","))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST, value = INDEX_PATH)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createIndex(@RequestBody IndexMappingDTO indexMapping) {
        indexManagementService.createIndex(indexMapping);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = INDEX_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteIndex(@RequestParam(INDEX_NAME_PARAM) String indexName) {
        indexManagementService.deleteIndex(indexName);
    }
}
