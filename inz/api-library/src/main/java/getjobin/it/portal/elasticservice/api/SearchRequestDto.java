package getjobin.it.portal.elasticservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder(builderClassName = "SearchRequestDtoBuilder")
@Data
@JsonDeserialize(builder = SearchRequestDto.SearchRequestDtoBuilder.class)
public class SearchRequestDto {

    private String index;
    private String searchText;
    private Map<String, Float> fieldsWithBoost;

    @JsonPOJOBuilder(withPrefix = "")
    public static class SearchRequestDtoBuilder { }
}
