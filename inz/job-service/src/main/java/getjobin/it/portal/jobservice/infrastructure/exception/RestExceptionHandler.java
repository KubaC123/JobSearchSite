package getjobin.it.portal.jobservice.infrastructure.exception;

import getjobin.it.portal.jobservice.api.ErrorMessageDto;
import getjobin.it.portal.jobservice.infrastructure.util.CurrentDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorMessageDto> handleConstraintViolation(ConstraintViolationException exception, WebRequest request) {
        Map<String, String> validationsByField = exception.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        constraintViolation -> constraintViolation.getPropertyPath().toString(),
                        constraintViolation -> constraintViolation.getMessage()));

        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body(ErrorMessageDto.builder()
                        .timeStamp(CurrentDate.get())
                        .message("Validation failed")
                        .validationsByField(validationsByField)
                        .build());
    }

    @ExceptionHandler({JobServiceException.class})
    public ResponseEntity<ErrorMessageDto> handleJobServiceIllegalArgumentException(JobServiceException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body(ErrorMessageDto.builder()
                        .timeStamp(CurrentDate.get())
                        .message(exception.getLocalizedMessage())
                        .build());
    }
}
