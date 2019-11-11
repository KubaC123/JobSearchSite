package getjobin.it.portal.elasticservice.client.control;

import getjobin.it.portal.elasticservice.api.DocumentEventDto;
import getjobin.it.portal.elasticservice.api.MappingEventDto;
import getjobin.it.portal.elasticservice.api.SearchRequestDto;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ESRequestBuilder {

    private static final String TIMEOUT_SEC = "1s";
    private static final int RETRY_COUNT = 2;
    private static final int RESULT_SIZE = 100;

    IndexRequest indexRequest(DocumentEventDto documentEvent) {
        return new IndexRequest(documentEvent.getIndex())
                .id(String.valueOf(documentEvent.getObjectId()))
                .opType(DocWriteRequest.OpType.CREATE)
                .source(documentEvent.getData(), XContentType.JSON)
                .timeout(TIMEOUT_SEC);
    }

    UpdateRequest updateRequest(DocumentEventDto documentEvent) {
        return new UpdateRequest(documentEvent.getIndex(), String.valueOf(documentEvent.getObjectId()))
                .doc(documentEvent.getData(), XContentType.JSON)
                .timeout(TIMEOUT_SEC)
                .retryOnConflict(RETRY_COUNT);
    }

    GetRequest documentExistsRequest(DocumentEventDto documentEvent) {
        return new GetRequest(documentEvent.getIndex(), String.valueOf(documentEvent.getObjectId()))
                .fetchSourceContext(new FetchSourceContext(false))
                .storedFields("_none_");
    }

    CreateIndexRequest createIndexRequest(MappingEventDto mappingEvent) {
        return new CreateIndexRequest(mappingEvent.getIndexName())
                .mapping(mappingEvent.getMapping(), XContentType.JSON);
    }

    DeleteIndexRequest deleteIndexRequest(String indexName) {
        return new DeleteIndexRequest(indexName);
    }

    PutMappingRequest putMappingRequest(MappingEventDto mappingEvent) {
        return new PutMappingRequest(mappingEvent.getIndexName())
                .source(mappingEvent.getMapping(), XContentType.JSON);
    }

    GetMappingsRequest getMappingRequest(List<String> indexNames) {
        GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
        indexNames.forEach(getMappingsRequest::indices);
        return getMappingsRequest;
    }

    GetIndexRequest getIndexRequest(String indexName) {
        return new GetIndexRequest(indexName)
                .local(false)
                .humanReadable(true)
                .includeDefaults(false);
    }

    SearchRequest fullTextSearch(SearchRequestDto searchRequest) {
        return new SearchRequest(searchRequest.getIndex()).source(getSearchSource(searchRequest));
    }

    private SearchSourceBuilder getSearchSource(SearchRequestDto searchRequest) {
        return new SearchSourceBuilder()
                .query(QueryBuilders.multiMatchQuery(searchRequest.getSearchText())
                        .fields(searchRequest.getFieldsWithBoost())
//                        .minimumShouldMatch("3")
                        .type(MultiMatchQueryBuilder.Type.MOST_FIELDS))
                .size(1000);
    }
}
