package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder(builderClassName = "JobSimpleDtoBuilder")
@Data
@JsonDeserialize(builder = JobSimpleDto.JobSimpleDtoBuilder.class)
public class JobSimpleDto {

    private Long id;
    private Float score;
    private String title;
    private String companyName;
    private Integer salaryMin;
    private Integer salaryMax;
    private List<String> cities;
    private List<String> techStacks;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JobSimpleDtoBuilder { }
}
