package getjobin.it.portal.elasticservice.client.control;

import api.DocumentEvent;
import api.MappingEvent;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ESRequestBuilder {

    private static final String TIMEOUT_SEC = "1s";
    private static final int RETRY_COUNT = 2;

    public IndexRequest indexRequest(DocumentEvent documentEvent) {
        return new IndexRequest(documentEvent.getIndex())
                .id(String.valueOf(documentEvent.getObjectId()))
                .opType(DocWriteRequest.OpType.fromString(documentEvent.getOperationType()))
                .source(documentEvent.getData(), XContentType.JSON)
                .timeout(TIMEOUT_SEC);
    }

    public UpdateRequest updateRequest(DocumentEvent documentEvent) {
        return new UpdateRequest(documentEvent.getIndex(), String.valueOf(documentEvent.getObjectId()))
                .doc(documentEvent.getData(), XContentType.JSON)
                .timeout(TIMEOUT_SEC)
                .retryOnConflict(RETRY_COUNT);
    }

    public CreateIndexRequest createIndexRequest(MappingEvent mappingEvent) {
        return new CreateIndexRequest(mappingEvent.getIndexName())
                .mapping(mappingEvent.getMapping(), XContentType.JSON);
    }

    public DeleteIndexRequest deleteIndexRequest(String indexName) {
        return new DeleteIndexRequest(indexName);
    }

    public PutMappingRequest putMappingRequest(MappingEvent mappingEvent) {
        return new PutMappingRequest(mappingEvent.getIndexName())
                .source(mappingEvent.getMapping(), XContentType.JSON);
    }

    public GetMappingsRequest getMappingRequest(List<String> indexNames) {
        GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
        indexNames.forEach(getMappingsRequest::indices);
        return getMappingsRequest;
    }

    public GetIndexRequest getIndexRequest(String indexName) {
        return new GetIndexRequest(indexName)
                .local(false)
                .humanReadable(true)
                .includeDefaults(false);
    }

    public SearchRequest fullTextSearch(String indexName, String searchText, List<String> fields) {
        return new SearchRequest(indexName).source(getSearchSource(fields, searchText));
    }

    private SearchSourceBuilder getSearchSource(List<String> fields, String searchText) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        fields.stream()
                .map(field -> new MatchQueryBuilder(field, searchText))
                .forEach(searchSourceBuilder::query);
        return searchSourceBuilder;
    }
}