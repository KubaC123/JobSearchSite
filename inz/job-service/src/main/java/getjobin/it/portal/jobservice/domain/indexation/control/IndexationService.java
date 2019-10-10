package getjobin.it.portal.jobservice.domain.indexation.control;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import getjobin.it.portal.elasticservice.api.DocumentEventDto;
import getjobin.it.portal.jobservice.domain.indexation.boundary.IndexationMapper;
import getjobin.it.portal.jobservice.domain.job.boundary.OperationType;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.infrastructure.config.KafkaTopic;
import getjobin.it.portal.jobservice.infrastructure.exception.JobServiceException;
import getjobin.it.portal.jobservice.infrastructure.query.boundary.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.BiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IndexationService {

    private static final int PARTITION_SIZE = 50;
    private static final int NUMBER_OF_THREADS = 10;

    private KafkaTopic kafkaTopic;

    private QueryService queryService;

    private IndexationMapper indexationMapper;

    private Executor executorService;

    private Map<String, BiConsumer<List<Long>, OperationType>> indexationConsumers = new HashMap<>();

    @Autowired
    public IndexationService(KafkaTopic kafkaTopic, QueryService queryService, IndexationMapper indexationMapper) {
        this.kafkaTopic = kafkaTopic;
        this.queryService = queryService;
        this.indexationMapper = indexationMapper;
    }

    @PostConstruct
    void init() {
        executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        indexationConsumers = ImmutableMap.of(Job.JOB_TYPE, jobIndexationConsumer);
    }

    private BiConsumer<List<Long>, OperationType> jobIndexationConsumer = (ids, operationType) -> {
        queryService.queryByIds(Job.class, ids).stream()
                .map(job -> indexationMapper.toIndexationEvent(job, operationType))
                .forEach(this::sendIndexationEvent);
    };

    private void sendIndexationEvent(DocumentEventDto event) {
        try {
            kafkaTopic.indexation()
                    .send(MessageBuilder
                            .withPayload(event)
                            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                            .build());
            logSuccess(event);
        } catch (Exception exception) {
            logException(event, exception);
            throw new JobServiceException("Exception during sending indexation event. Check server log for details.");
        }
    }

    private void logSuccess(DocumentEventDto event) {
        log.info("Event with id {} and operation type {} has been send on kafka topic",
                event.getObjectId(), event.getOperationType());
    }

    private void logException(DocumentEventDto event, Exception exception) {
        log.info("Exception during sending event with id {} and operation type {} on kafka topic. {}",
                event.getObjectId(), event.getOperationType(), exception);
    }

    @Async
    public void indexObjectsAsync(List<Long> ids, String objectType, OperationType operationType) {
        BiConsumer<List<Long>, OperationType> indexationConsumer = getProperConsumer(objectType);

        List<CompletableFuture<Void>> futurePartitionsToIndex = getFuturePartitionsToIndex(ids, operationType, indexationConsumer);

        CompletableFuture.allOf(futurePartitionsToIndex.toArray(new CompletableFuture[futurePartitionsToIndex.size()]))
                .exceptionally(exception -> {
                    log.warn("Exception during partition indexation", exception);
                    return null;
                });
    }

    private BiConsumer<List<Long>, OperationType> getProperConsumer(String objectType) {
        if(!indexationConsumers.containsKey(objectType)) {
            throw new JobServiceException("No indexation consumer specified for type " + objectType);
        }
        return indexationConsumers.get(objectType);
    }

    private List<CompletableFuture<Void>> getFuturePartitionsToIndex(List<Long> ids,
                                                                     OperationType operationType,
                                                                     BiConsumer<List<Long>, OperationType> indexationConsumer) {
        return Lists.partition(ids, PARTITION_SIZE).stream()
                .map(partition -> CompletableFuture.runAsync(() -> indexationConsumer.accept(partition, operationType), executorService))
                .collect(Collectors.toList());
    }
}
