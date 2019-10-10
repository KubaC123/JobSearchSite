package getjobin.it.portal.elasticservice.infrastructure.exception;

import getjobin.it.portal.elasticservice.infrastructure.CurrentDate;
import getjobin.it.portal.jobservice.api.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ElasticServiceException.class})
    public ResponseEntity<ErrorMessageDto> handleElasticServiceException(ElasticServiceException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body(ErrorMessageDto.builder()
                        .timeStamp(CurrentDate.get())
                        .message(exception.getMessage())
                        .build());
    }
}
