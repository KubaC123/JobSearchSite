package getjobin.it.portal.jobservice.api.domain.event;

import getjobin.it.portal.jobservice.domain.job.boundary.OperationType;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class JobEvent {

    private Long jobId;
    private OperationType operationType;
}
