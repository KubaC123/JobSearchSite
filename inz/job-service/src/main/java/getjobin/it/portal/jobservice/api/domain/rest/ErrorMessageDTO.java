package getjobin.it.portal.jobservice.api.domain.rest;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

@Builder
@Value
public class ErrorMessageDTO {

    private Date timeStamp;
    private HttpStatus status;
    private String message;
    private Map<String, String> validationsByField;
}
