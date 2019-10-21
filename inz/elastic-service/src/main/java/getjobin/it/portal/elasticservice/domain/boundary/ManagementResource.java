package getjobin.it.portal.elasticservice.domain.boundary;

import getjobin.it.portal.elasticservice.api.MappingEventDto;
import getjobin.it.portal.elasticservice.client.control.ESJavaClient;
import getjobin.it.portal.elasticservice.client.control.ManagementMapper;
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
@RequestMapping(value = "management")
public class ManagementResource {

    private static final String INDEX_PATH = "index";
    private static final String INDEX_NAME_PARAM = "name";

    private ESJavaClient esJavaClient;

    private ManagementMapper managementMapper;

    @Autowired
    public ManagementResource(ESJavaClient esJavaClient, ManagementMapper managementMapper) {
        this.esJavaClient = esJavaClient;
        this.managementMapper = managementMapper;
    }

    @RequestMapping(method = RequestMethod.GET, value = INDEX_PATH)
    public List<MappingEventDto> getMappings(@RequestParam(INDEX_NAME_PARAM) String commaSeparatedIndexesNames) {
        Map<String, MappingMetaData> mappings = esJavaClient.getMapping(getIndexesAsList(commaSeparatedIndexesNames));
        return managementMapper.toIndexMappingDTOs(mappings);
    }

    private List<String> getIndexesAsList(String commaSeparatedIndexesNames) {
        return Arrays.stream(commaSeparatedIndexesNames.split(","))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST, value = INDEX_PATH)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createIndex(@RequestBody MappingEventDto indexMapping) {
        esJavaClient.createIndex(indexMapping);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = INDEX_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteIndex(@RequestParam(INDEX_NAME_PARAM) String indexName) {
        esJavaClient.deleteIndex(indexName);
    }
}
