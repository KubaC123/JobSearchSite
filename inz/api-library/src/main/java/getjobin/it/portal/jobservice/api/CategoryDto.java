package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "CategoryDtoBuilder")
@Data
@JsonDeserialize(builder = CategoryDto.CategoryDtoBuilder.class)
public class CategoryDto {

    private Long id;
    private String name;
    private Integer jobCounter;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CategoryDtoBuilder { }
}
