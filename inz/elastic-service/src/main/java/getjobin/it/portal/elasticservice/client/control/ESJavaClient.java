package getjobin.it.portal.elasticservice.client.control;

import getjobin.it.portal.elasticservice.api.DocumentDto;
import getjobin.it.portal.elasticservice.api.DocumentEventDto;
import getjobin.it.portal.elasticservice.api.MappingEventDto;
import getjobin.it.portal.elasticservice.api.SearchRequestDto;
import getjobin.it.portal.elasticservice.api.SearchResultDto;
import getjobin.it.portal.elasticservice.infrastructure.exception.UncheckedFunction;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
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
import java.util.Set;

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

    public void indexDocument(DocumentEventDto documentEvent) {
        indexDocument().unchecked(requestBuilder.indexRequest(documentEvent));
        log.info("[INDEXATION] Indexed document with id: {}, on index: {}", documentEvent.getObjectId(), documentEvent.getIndex());
    }

    private UncheckedFunction<IndexRequest, IndexResponse> indexDocument() {
        return indexRequest -> client.index(indexRequest, RequestOptions.DEFAULT);
    }

    public void updateDocument(DocumentEventDto documentEvent) {
        updateDocument().unchecked(requestBuilder.updateRequest(documentEvent));
        log.info("[INDEXATION] Updated document with id: {}, on index: {}", documentEvent.getObjectId(), documentEvent.getIndex());
    }

    private UncheckedFunction<UpdateRequest, UpdateResponse> updateDocument() {
        return updateRequest -> client.update(updateRequest, RequestOptions.DEFAULT);
    }

    public boolean documentExists(DocumentEventDto documentEvent) {
        return documentExists().unchecked(requestBuilder.documentExistsRequest(documentEvent));
    }

    private UncheckedFunction<GetRequest, Boolean> documentExists() {
        return getRequest -> client.exists(getRequest, RequestOptions.DEFAULT);
    }

    public void createIndex(MappingEventDto mappingEvent) {
        createIndex().unchecked(requestBuilder.createIndexRequest(mappingEvent));
        log.info("[INDEX] Index {} has been created", mappingEvent.getIndexName());
    }

    private UncheckedFunction<CreateIndexRequest, CreateIndexResponse> createIndex() {
        return createIndexRequest -> client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    public void deleteIndex(String indexName) {
        deleteIndex().unchecked(requestBuilder.deleteIndexRequest(indexName));
        log.info("[INDEX] Index {} has been deleted", indexName);
    }

    private UncheckedFunction<DeleteIndexRequest, AcknowledgedResponse> deleteIndex() {
        return deleteIndexRequest -> client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
    }

    public void putMapping(MappingEventDto mappingEvent) {
        putMapping().unchecked(requestBuilder.putMappingRequest(mappingEvent));
        log.info("[INDEX] Mapping for index {} has been updated", mappingEvent.getIndexName());
    }

    private UncheckedFunction<PutMappingRequest, AcknowledgedResponse> putMapping() {
        return putMappingRequest -> client.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
    }

    public Map<String, MappingMetaData> getMapping(List<String> indexNames) {
        return getMapping().unchecked(requestBuilder.getMappingRequest(indexNames)).mappings();
    }

    private UncheckedFunction<GetMappingsRequest, GetMappingsResponse> getMapping() {
        return getMappingsRequest -> client.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);
    }

    public Boolean indexExist(String indexName) {
        return indexExist().unchecked(requestBuilder.getIndexRequest(indexName));
    }

    private UncheckedFunction<GetIndexRequest, Boolean> indexExist() {
        return getIndexRequest -> client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    public SearchResultDto fullTextSearch(SearchRequestDto searchRequest) {
        SearchResponse response = search().unchecked(requestBuilder.fullTextSearch(searchRequest));
        List<DocumentDto> foundDocuments = getFoundDocuments(response);
        logSearchDetails(searchRequest, response, foundDocuments);
        return buildSearchResult(foundDocuments);
    }

    private void logSearchDetails(SearchRequestDto searchRequest, SearchResponse response, List<DocumentDto> documents) {
        logSearchDetails(
                searchRequest.getIndex(),
                searchRequest.getSearchText(),
                searchRequest.getFieldsWithBoost().keySet(),
                response,
                documents.size());
    }

    private UncheckedFunction<SearchRequest, SearchResponse> search() {
        return searchRequest -> client.search(searchRequest, RequestOptions.DEFAULT);
    }

    private List<DocumentDto> getFoundDocuments(SearchResponse response) {
        List<DocumentDto> foundDocuments = new ArrayList<>();
        for(SearchHit hit : response.getHits().getHits()) {
            foundDocuments.add(DocumentDto.builder()
                    .score(hit.getScore())
                    .objectId(Long.valueOf(hit.getId()))
                    .data(hit.getSourceAsMap())
                    .build());
        }
        return foundDocuments;
    }

    private void logSearchDetails(String indexName, String searchText, Set<String> fields, SearchResponse response, int foundDocumentsCount) {
        log.info("[SEARCH] Searched for index name: {}, text: {}, fields {}. \n" +
                        "Search response details: \n" +
                        "Found documents: {} \n" +
                        "Response status: {} \n" +
                        "Time took: {} \n" +
                        "Terminated early: {} \n" +
                        "Timed out: {} \n" +
                        "Total shards: {} \n" +
                        "Successful shards: {} \n" +
                        "Failed shard: {} \n",
                indexName,
                searchText,
                fields,
                foundDocumentsCount,
                response.status(),
                response.getTook(),
                response.isTerminatedEarly(),
                response.isTimedOut(),
                response.getTotalShards(),
                response.getSuccessfulShards(),
                response.getFailedShards());
        // todo handle failed shards
    }

    private SearchResultDto buildSearchResult(List<DocumentDto> documents) {
        return SearchResultDto.builder()
                .count(documents.size())
                .documents(documents)
                .build();
    }

}
