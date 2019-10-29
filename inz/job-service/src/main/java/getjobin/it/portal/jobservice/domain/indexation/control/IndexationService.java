package getjobin.it.portal.jobservice.domain.indexation.control;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import getjobin.it.portal.elasticservice.api.DocumentEventDto;
import getjobin.it.portal.jobservice.domain.ManagedEntity;
import getjobin.it.portal.jobservice.domain.indexation.boundary.IndexationMapper;
import getjobin.it.portal.jobservice.domain.job.control.OperationType;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.search.boundary.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
@Slf4j
public class IndexationService {

    private static final int PARTITION_SIZE = 100;
    private static final int NUMBER_OF_THREADS = 10;

    @Autowired
    private QueryService queryService;

    @Autowired
    private IndexationMapper indexationMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private Executor executorService;

    private Map<Class<? extends ManagedEntity>, Consumer<List<? extends ManagedEntity>>> indexationConsumers;

    @PostConstruct
    void init() {
        executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        indexationConsumers = ImmutableMap.of(Job.class, jobIndexationConsumer);
    }

    private Consumer<List<? extends ManagedEntity>> jobIndexationConsumer = jobs ->
        sendIndexationEvents(indexationMapper.toDocumentEventDtos((List<Job>)jobs, OperationType.UPDATE));

    private void sendIndexationEvents(List<DocumentEventDto> events) {
        for(DocumentEventDto event : events) {
            eventPublisher.publishEvent(event);
        }
    }

    @Async
    public <T extends ManagedEntity> void indexAllObjectsAsync(Class<T> entityClass) {
        Long count = queryService.countObjects(entityClass);
        CompletableFuture[] futurePartitions = getFuturePartitions(entityClass, count);
        CompletableFuture.allOf(futurePartitions).exceptionally(handleIndexationException);
;    }

    private <T extends ManagedEntity> CompletableFuture[] getFuturePartitions(Class<T> entityClass, Long count) {
        return Stream.iterate(0, i -> i + PARTITION_SIZE)
                .limit(count/PARTITION_SIZE + 1)
                .map(i -> futureIndexPartition(i, entityClass))
                .toArray(CompletableFuture[]::new);
    }

    private <T extends ManagedEntity> CompletableFuture<Void> futureIndexPartition(Integer startRow, Class<T> entityClass) {
        return CompletableFuture.runAsync(() -> indexPartition(startRow, entityClass), executorService);
    }

    private <T extends ManagedEntity> void indexPartition(Integer startRow, Class<T> entityClass) {
        List<T> objects = queryService.queryPartition(startRow, startRow + PARTITION_SIZE, entityClass);
        indexationConsumers.get(entityClass).accept(objects);
    }

    private Function<Throwable, Void> handleIndexationException = ex -> {
        log.warn("[INDEXATION] indexation failed, stack trace: \n{}", (Object) ex.getStackTrace());
        return null;
    };

    @Async
    public <T extends ManagedEntity> void indexGivenObjectsAsync(List<Long> ids, Class<T> entityClass) {
        CompletableFuture[] futurePartitions = getFuturePartitions(ids, entityClass);
        CompletableFuture.allOf(futurePartitions).exceptionally(handleIndexationException);
    }

    private <T extends ManagedEntity> CompletableFuture[] getFuturePartitions(List<Long> ids, Class<T> entityClass) {
        return Lists.partition(ids, PARTITION_SIZE).stream()
                .map(partition -> futureIndexPartition(partition, entityClass))
                .toArray(CompletableFuture[]::new);
    }

    private <T extends ManagedEntity> CompletableFuture<Void> futureIndexPartition(List<Long> ids, Class<T> entityClass) {
        return CompletableFuture.runAsync(() -> indexPartition(ids, entityClass), executorService);
    }

    private <T extends ManagedEntity> void indexPartition(List<Long> ids, Class<T> entityClass) {
        List<T> objects = queryService.queryByIds(ids, entityClass);
        indexationConsumers.get(entityClass).accept(objects);
    }
}
