package getjobin.it.portal.jobservice.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "LocationDtoBuilder")
@Data
@JsonDeserialize(builder = LocationDto.LocationDtoBuilder.class)
public class LocationDto {

    private Long id;
    private String city;
    private String street;
    private String countryCode;
    private String countryName;
    private String postCode;
    private Float latitude;
    private Float longitude;

    @JsonPOJOBuilder(withPrefix = "")
    public static class LocationDtoBuilder { }
}
