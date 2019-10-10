package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "TechStackDtoBuilder", toBuilder = true)
@Data
@JsonDeserialize(builder = TechStackDto.TechStackDtoBuilder.class)
public class TechStackDto {

    private Long id;
    private String name;

    @JsonPOJOBuilder(withPrefix = "")
    public static class TechStackDtoBuilder { }
}
