package getjobin.it.portal.jobservice.domain.job.control;

import getjobin.it.portal.jobservice.api.JobEventDto;
import getjobin.it.portal.jobservice.domain.category.control.CategoryService;
import getjobin.it.portal.jobservice.domain.job.boundary.OperationType;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.technology.control.TechnologyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.text.MessageFormat;
import java.util.Optional;

@Component
@Slf4j
public class JobEventListener {

    private JobService jobService;

    private TechnologyService technologyService;

    private CategoryService categoryService;

    @Autowired
    public JobEventListener(JobService jobService, TechnologyService technologyService, CategoryService categoryService) {
        this.jobService = jobService;
        this.technologyService = technologyService;
        this.categoryService = categoryService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void processJobEvent(JobEventDto event) {
        try {
            OperationType operationType = OperationType.valueOf(event.getOperationType());
            log.info(MessageFormat.format("Received job event with id {0} and operation type {1}", event.getJobId(), operationType.getLiteral()));
            switch (operationType) {
                case CREATE:
                    processJobCreate(event.getJobId());
                    break;
                case UPDATE:
                    processJobUpdate(event.getJobId());
                    break;
                case DELETE:
                    processJobDelete(event.getJobId());
                    break;
            }
        } catch (Exception exception) {
            log.warn("Exception during processing job event {}.\n {}", event, exception);
        }
    }

    private void processJobCreate(Long jobId) {
        Job createdJob = jobService.getById(jobId);
        Optional.ofNullable(createdJob.getCategory())
                .ifPresent(categoryService::incrementJobCounter);
        Optional.ofNullable(createdJob.getTechnology())
                .ifPresent(technologyService::incrementJobCounter);
    }

    private void processJobUpdate(Long jobId) {

    }

    private void processJobDelete(Long jobId) {
        Job removedJob = jobService.getById(jobId);
        Optional.ofNullable(removedJob.getCategory())
                .ifPresent(categoryService::decrementJobCounter);
        Optional.ofNullable(removedJob.getTechnology())
                .ifPresent(technologyService::decrementJobCounter);
    }
}
