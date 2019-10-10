package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "JobEventDtoBuilder")
@Data
@JsonDeserialize(builder = JobEventDto.JobEventDtoBuilder.class)
public class JobEventDto {

    private Long jobId;
    private String operationType;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JobEventDtoBuilder { }
}
