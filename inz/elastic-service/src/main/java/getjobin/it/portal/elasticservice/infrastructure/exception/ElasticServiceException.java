package getjobin.it.portal.elasticservice.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class ElasticServiceException extends RuntimeException {

    public ElasticServiceException(String message) {
        super(message);
    }
}
