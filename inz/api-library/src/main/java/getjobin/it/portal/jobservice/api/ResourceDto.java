package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.net.URI;

@Builder(builderClassName = "ResourceDtoBuilder")
@Data
@JsonDeserialize(builder = ResourceDto.ResourceDtoBuilder.class)
public class ResourceDto {

    private String objectType;
    private Long objectId;
    private URI resourceURI;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ResourceDtoBuilder { }
}
