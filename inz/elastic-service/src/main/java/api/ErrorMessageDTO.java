package api;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Builder
@Value
public class ErrorMessageDTO {

    private Date timeStamp;
    private HttpStatus status;
    private String message;
}
