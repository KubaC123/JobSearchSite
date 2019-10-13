package getjobin.it.portal.common.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Builder(builderClassName = "ErrorMessageDtoBuilder")
@Data
@JsonDeserialize(builder = ErrorMessageDto.ErrorMessageDtoBuilder.class)
public class ErrorMessageDto {

    private Date timeStamp;
    private String message;
    private Map<String, String> validationsByField;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ErrorMessageDtoBuilder { }
}
