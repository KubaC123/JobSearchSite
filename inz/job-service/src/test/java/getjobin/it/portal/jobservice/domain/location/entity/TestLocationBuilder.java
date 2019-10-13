package getjobin.it.portal.jobservice.domain.location.entity;

import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestLocationBuilder {

    public static final String CITY = "Wrocław";
    public static final String STREET = "Grunwaldzka";
    public static final String COUNTRY_CODE = "PL";
    public static final String COUNTRY_NAME = "Polska";
    public static final String POST_CODE = "50-231";
    public static final Float LONGITUDE = 34.3231f;
    public static final Float LATITUDE = 45.4324f;

    public static Location buildValidLocation() {
        return Location.builder()
                .city(CITY)
                .street(STREET)
                .countryCode(COUNTRY_CODE)
                .countryName(COUNTRY_NAME)
                .postCode(POST_CODE)
                .longitude(LONGITUDE)
                .latitude(LATITUDE)
                .build();
    }

    public static List<Location> buildValidLocations(int numberOfLocations) {
        return IntStream.range(0, numberOfLocations)
                .mapToObj(nr -> Location.builder()
                        .city(CITY + nr)
                        .street(STREET + nr)
                        .countryCode(COUNTRY_CODE + nr)
                        .countryName(COUNTRY_NAME + nr)
                        .postCode(POST_CODE + nr)
                        .longitude(LONGITUDE + nr)
                        .latitude(LATITUDE + nr)
                        .build())
                .collect(Collectors.toList());
    }

    public static Location buildLocationWithNullCity() {
        return Location.toBuilder(buildValidLocation())
                .city(null)
                .build();
    }

    public static Location buildLocationWithNullStreet() {
        return Location.toBuilder(buildValidLocation())
                .street(null)
                .build();
    }

    public static Location buildLocationWithNullCoordinates() {
        return Location.toBuilder(buildValidLocation())
                .longitude(null)
                .latitude(null)
                .build();
    }

    public static Location buildLocationWithEmptyCity() {
        return Location.toBuilder(buildValidLocation())
                .city("")
                .build();
    }

    public static Location buildLocationWithEmptyStreet() {
        return Location.toBuilder(buildValidLocation())
                .street("")
                .build();
    }
}
