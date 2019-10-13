package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "JobLocationDtoBuilder")
@Data
@JsonDeserialize(builder = JobLocationDto.JobLocationDtoBuilder.class)
public class JobLocationDto {

    private LocationDto location;
    private Boolean remote;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JobLocationDtoBuilder { }
}
