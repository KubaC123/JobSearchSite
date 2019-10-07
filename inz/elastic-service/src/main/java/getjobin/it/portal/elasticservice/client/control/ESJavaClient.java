package getjobin.it.portal.elasticservice.client.control;

import api.DocumentDTO;
import api.DocumentEvent;
import api.MappingEvent;
import api.SearchResult;
import getjobin.it.portal.elasticservice.client.control.ESRequestBuilder;
import getjobin.it.portal.elasticservice.infrastructure.exception.ElasticSearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ESJavaClient {

    private ESRequestBuilder requestBuilder;

    private RestHighLevelClient client;

    @Autowired
    public ESJavaClient(ESRequestBuilder requestBuilder, RestHighLevelClient client) {
        this.requestBuilder = requestBuilder;
        this.client = client;

    }

    public void indexDocument(DocumentEvent documentEvent) {
        indexDocument().unchecked(requestBuilder.indexRequest(documentEvent));
    }

    private ElasticSearchRequest<IndexRequest, IndexResponse> indexDocument() {
        return indexRequest -> client.index(indexRequest, RequestOptions.DEFAULT);
    }

    public void updateDocument(DocumentEvent documentEvent) {
        updateDocument().unchecked(requestBuilder.updateRequest(documentEvent));
    }

    private ElasticSearchRequest<UpdateRequest, UpdateResponse> updateDocument() {
        return updateRequest -> client.update(updateRequest, RequestOptions.DEFAULT);
    }

    public void createIndex(MappingEvent mappingEvent) {
        createIndex().unchecked(requestBuilder.createIndexRequest(mappingEvent));
    }

    private ElasticSearchRequest<CreateIndexRequest, CreateIndexResponse> createIndex() {
        return createIndexRequest -> client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    public void deleteIndex(String indexName) {
        deleteIndex().unchecked(requestBuilder.deleteIndexRequest(indexName));
    }

    private ElasticSearchRequest<DeleteIndexRequest, AcknowledgedResponse> deleteIndex() {
        return deleteIndexRequest -> client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
    }

    public void putMapping(MappingEvent mappingEvent) {
        putMapping().unchecked(requestBuilder.putMappingRequest(mappingEvent));
    }

    private ElasticSearchRequest<PutMappingRequest, AcknowledgedResponse> putMapping() {
        return putMappingRequest -> client.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
    }

    public Map<String, MappingMetaData> getMapping(List<String> indexNames) {
        return getMapping().unchecked(requestBuilder.getMappingRequest(indexNames)).mappings();
    }

    private ElasticSearchRequest<GetMappingsRequest, GetMappingsResponse> getMapping() {
        return getMappingsRequest -> client.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);
    }

    public Boolean indexExist(String indexName) {
        return indexExist().unchecked(requestBuilder.getIndexRequest(indexName));
    }

    private ElasticSearchRequest<GetIndexRequest, Boolean> indexExist() {
        return getIndexRequest -> client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    public SearchResult fullTextSearch(String indexName, String searchText, List<String> fields) {
        SearchResponse response = search().unchecked(requestBuilder.fullTextSearch(indexName, searchText, fields));
        logSearchDetails(response);
        List<DocumentDTO> foundDocuments = new ArrayList<>();
        for(SearchHit hit : response.getHits().getHits()) {
            foundDocuments.add(DocumentDTO.builder()
                    .score(hit.getScore())
                    .objectId(Long.valueOf(hit.getId()))
                    .data(hit.getSourceAsMap())
                    .build());
        }
        return SearchResult.builder()
                .count(foundDocuments.size())
                .documents(foundDocuments)
                .build();
    }

    private ElasticSearchRequest<SearchRequest, SearchResponse> search() {
        return searchRequest -> client.search(searchRequest, RequestOptions.DEFAULT);
    }

    private void logSearchDetails(SearchResponse response) {
        log.info("Search request details: \n" +
                        "Response status: {} \n" +
                        "Time took: {} \n" +
                        "Terminated early: {} \n" +
                        "Timed out: {} \n" +
                        "Total shards: {} \n" +
                        "Successful shards: {} \n" +
                        "Failed shard: {} \n",
                response.status(),
                response.getTook(),
                response.isTerminatedEarly(),
                response.isTimedOut(),
                response.getTotalShards(),
                response.getSuccessfulShards(),
                response.getFailedShards());
        // todo handle failed shards
    }
}
