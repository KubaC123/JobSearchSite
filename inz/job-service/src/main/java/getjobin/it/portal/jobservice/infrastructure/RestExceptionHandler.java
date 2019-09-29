package getjobin.it.portal.jobservice.infrastructure;

import getjobin.it.portal.jobservice.api.domain.ErrorMessageDTO;
import getjobin.it.portal.jobservice.infrastructure.exceptions.JobServiceIllegalArgumentException;
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
    public ResponseEntity<ErrorMessageDTO> handleConstraintViolation(ConstraintViolationException exception, WebRequest request) {
        Map<String, String> validationsByField = exception.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        constraintViolation -> constraintViolation.getPropertyPath().toString(),
                        constraintViolation -> constraintViolation.getMessage()));

        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body(ErrorMessageDTO.builder()
                        .timeStamp(CurrentDate.get())
                        .status(HttpStatus.PRECONDITION_FAILED)
                        .message("Validation failed")
                        .validationsByField(validationsByField)
                        .build());
    }

    @ExceptionHandler({JobServiceIllegalArgumentException.class})
    public ResponseEntity<ErrorMessageDTO> handleJobServiceIllegalArgumentException(JobServiceIllegalArgumentException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body(ErrorMessageDTO.builder()
                        .timeStamp(CurrentDate.get())
                        .status(HttpStatus.PRECONDITION_FAILED)
                        .message(exception.getLocalizedMessage())
                        .build());
    }

}
