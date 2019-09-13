package getjobin.it.portal.jobservice.domain.technology.entity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestTechnologyBuilder {

    public static final String NAME = "Java";
    public static final String IMAGE_URL = "lalala";
    private static final String UPDATE = "update";

    public static Technology buildValidTechnology() {
        return Technology.builder()
                .withName(NAME)
                .withImageUrl(IMAGE_URL)
                .build();
    }

    public static List<Technology> buildValidTechnologies(int numberOfTechnologies) {
        return IntStream.rangeClosed(1, numberOfTechnologies)
                .mapToObj(index -> Technology.builder()
                        .withName(NAME + index)
                        .withImageUrl(IMAGE_URL + index)
                        .build())
                .collect(Collectors.toList());
    }

    public static Technology buildTechnologyWithEmptyName() {
        return Technology.builder()
                .withName("")
                .withImageUrl(IMAGE_URL)
                .build();
    }

    public static Technology buildTechnologyWithNullName() {
        return Technology.builder()
                .withImageUrl(IMAGE_URL)
                .build();
    }

    public static Technology buildValidUpdatedTechnology(Technology createdTechnology) {
        return Technology.toBuilder(createdTechnology)
                .withName(NAME + UPDATE)
                .withImageUrl(IMAGE_URL + UPDATE)
                .build();
    }
}
