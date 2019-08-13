package getjobin.it.portal.jobservice.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class JobServiceIllegalArgumentException extends RuntimeException {

    public JobServiceIllegalArgumentException(String message) {
        super(message);
    }
}
