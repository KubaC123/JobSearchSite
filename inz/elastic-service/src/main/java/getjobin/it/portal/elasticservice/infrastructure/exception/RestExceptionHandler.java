package getjobin.it.portal.elasticservice.infrastructure.exception;

import api.ErrorMessageDTO;
import getjobin.it.portal.elasticservice.infrastructure.CurrentDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ElasticServiceException.class})
    public ResponseEntity<ErrorMessageDTO> handleElasticServiceException(ElasticServiceException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body(ErrorMessageDTO.builder()
                        .timeStamp(CurrentDate.get())
                        .status(HttpStatus.PRECONDITION_FAILED)
                        .message(exception.getMessage())
                        .build());
    }
}
