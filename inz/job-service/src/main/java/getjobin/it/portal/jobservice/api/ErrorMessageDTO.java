package getjobin.it.portal.jobservice.api;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

@Builder @Getter
public class ErrorMessageDTO {

    private Date timeStamp;
    private HttpStatus status;
    private String message;
    private Map<String, String> validationsByField;

}
