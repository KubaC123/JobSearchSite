package getjobin.it.portal.elasticservice.client.management.control;

import api.IndexMappingDTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ElasticManagementService {

    private RestHighLevelClient client;

    @Autowired
    public ElasticManagementService(RestHighLevelClient client) {
        this.client = client;
    }

    public void createIndex(String indexName) {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        try {
            client.indices().create(request, RequestOptions.DEFAULT);
        } catch (Exception exception) {
            log.warn("Exception when trying to create index " + indexName, exception);
        }
    }

    public void deleteIndex(String indexName) {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        try {
            client.indices().delete(request, RequestOptions.DEFAULT);
        } catch (Exception exception) {
            log.warn("Exception when trying to delete index " + indexName, exception);
        }
    }

    public void putMapping(IndexMappingDTO indexMapping) {
        PutMappingRequest request = new PutMappingRequest(indexMapping.getIndexName())
                .source(indexMapping.getMapping(), XContentType.JSON);
        try {
            client.indices().putMapping(request, RequestOptions.DEFAULT);
        } catch (Exception exception) {
            log.warn("Exception when trying to put mapping for index " + indexMapping.getIndexName(), exception);
        }
    }

    public Map<String, MappingMetaData> getMappings(List<String> indexNames) {
        GetMappingsRequest request = new GetMappingsRequest();
        indexNames.forEach(request::indices);
        try {
            GetMappingsResponse response = client.indices().getMapping(request, RequestOptions.DEFAULT);
            return response.mappings();
        } catch (Exception exception) {
            log.warn("Exception when trying to get mappings for index " + indexNames, exception);
            return Collections.emptyMap();
        }
    }

    public boolean indexExist(String indexName) {
        GetIndexRequest request = new GetIndexRequest(indexName)
                .local(false)
                .humanReadable(true)
                .includeDefaults(false);
        try {
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (Exception exception) {
            log.warn("Exception when trying to check if index " + indexName + " exists.", exception);
            return false;
        }
    }
}
