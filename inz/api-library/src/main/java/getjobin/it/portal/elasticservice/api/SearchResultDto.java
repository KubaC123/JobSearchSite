package getjobin.it.portal.elasticservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder(builderClassName = "SearchResultDtoBuilder")
@Data
@JsonDeserialize(builder = SearchResultDto.SearchResultDtoBuilder.class)
public class SearchResultDto {

    Integer count;
    List<DocumentDto> documents;

    @JsonPOJOBuilder(withPrefix = "")
    public static class SearchResultDtoBuilder { }
}
