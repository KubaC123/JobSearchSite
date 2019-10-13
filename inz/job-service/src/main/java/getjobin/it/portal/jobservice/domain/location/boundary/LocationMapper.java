package getjobin.it.portal.jobservice.domain.location.boundary;

import getjobin.it.portal.jobservice.api.LocationDto;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public Location toEntity(LocationDto locationDto) {
        return Location.builder()
                .id(locationDto.getId())
                .city(locationDto.getCity())
                .street(locationDto.getStreet())
                .countryCode(locationDto.getCountryCode())
                .countryName(locationDto.getCountryName())
                .postCode(locationDto.getPostCode())
                .longitude(locationDto.getLongitude())
                .latitude(locationDto.getLatitude())
                .build();
    }

    public LocationDto toDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .city(location.getCity())
                .street(location.getStreet())
                .countryCode(location.getCountryCode())
                .countryName(location.getCountryName())
                .postCode(location.getPostCode())
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build();
    }
}
