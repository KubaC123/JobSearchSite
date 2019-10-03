package getjobin.it.portal.elasticservice.client.document;

import api.IndexationEventDTO;
import getjobin.it.portal.elasticservice.infrastructure.exception.ElasticServiceException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DocumentService {

    private static final String TIMEOUT_SEC = "1s";
    private static final int RETRY_COUNT = 2;

    private RestHighLevelClient client;

    @Autowired
    public DocumentService(RestHighLevelClient client) {
        this.client = client;
    }

    public void createDocument(IndexationEventDTO indexationEvent) {
        IndexRequest request = new IndexRequest(indexationEvent.getIndex())
                .id(String.valueOf(indexationEvent.getObjectId()))
                .opType(DocWriteRequest.OpType.fromString(indexationEvent.getOperationType()))
                .source(indexationEvent.getData(), XContentType.JSON)
                .timeout(TIMEOUT_SEC);
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (Exception exception) {
            // todo handle ElasticSearchException
            log.warn("Exception during object indexation from event {}. \n {}", indexationEvent, exception);
            throw new ElasticServiceException("Indexation error");
        }
    }

    public void updateDocument(IndexationEventDTO indexationEvent) {
        UpdateRequest request = new UpdateRequest(indexationEvent.getIndex(), String.valueOf(indexationEvent.getObjectId()))
                .doc(indexationEvent.getData(), XContentType.JSON)
                .timeout(TIMEOUT_SEC)
                .retryOnConflict(RETRY_COUNT);
        try {
            client.update(request, RequestOptions.DEFAULT);
        } catch (Exception exception) {
            log.warn("Exception during object indexation from event {}. \n {}", indexationEvent, exception);
            throw new ElasticServiceException("Indexation error");
        }
    }
}
