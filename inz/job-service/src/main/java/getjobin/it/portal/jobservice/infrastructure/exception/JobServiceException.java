package getjobin.it.portal.jobservice.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class JobServiceException extends RuntimeException {

    public JobServiceException(String message) {
        super(message);
    }
}
