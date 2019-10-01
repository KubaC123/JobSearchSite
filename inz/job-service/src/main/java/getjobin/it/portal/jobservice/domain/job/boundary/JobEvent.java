package getjobin.it.portal.jobservice.domain.job.boundary;

import getjobin.it.portal.jobservice.domain.event.OperationType;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class JobEvent {

    private Long jobId;
    private OperationType operationType;
}
