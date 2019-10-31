package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder(builderClassName = "JobSearchDtoBuilder")
@Data
@JsonDeserialize(builder = JobSearchDto.JobSearchDtoBuilder.class)
public class JobSearchDto {

    private String searchText;
    private List<String> attributes;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JobSearchDtoBuilder { }
}
