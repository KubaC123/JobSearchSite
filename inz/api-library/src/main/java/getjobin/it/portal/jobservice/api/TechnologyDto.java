package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "TechnologyDtoBuilder")
@Data
@JsonDeserialize(builder = TechnologyDto.TechnologyDtoBuilder.class)
public class TechnologyDto {

    private Long id;
    private String name;
    private String imageUrl;

    @JsonPOJOBuilder(withPrefix = "")
    public static class TechnologyDtoBuilder { }
}
