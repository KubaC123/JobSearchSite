package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "CompanyDtoBuilder")
@Data
@JsonDeserialize(builder = CompanyDto.CompanyDtoBuilder.class)
public class CompanyDto {

    private Long id;
    private String name;
    private String webSiteUrl;
    private String size;
    private String logoUrl;
    private Integer establishment;
    private String description;
    private String instagramUrl;
    private String facebookUrl;
    private String linkedinUrl;
    private String twitterUrl;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CompanyDtoBuilder { }
}
