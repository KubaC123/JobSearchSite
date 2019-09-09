package getjobin.it.portal.jobservice.domain.technology;

import getjobin.it.portal.jobservice.domain.technology.entity.Technology;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestTechnologyBuilder {

    public static final String TEST_TECHNOLOGY_NAME = "Java";
    public static final String TEST_TECHNOLOGY_IMAGE_URL = "lalala";
    private static final String UPDATE = "update";

    public static Technology buildValidTechnology() {
        return Technology.builder()
                .withName(TEST_TECHNOLOGY_NAME)
                .withImageUrl(TEST_TECHNOLOGY_IMAGE_URL)
                .build();
    }

    public static List<Technology> buildValidTechnologies(int numberOfTechnologies) {
        return IntStream.rangeClosed(1, numberOfTechnologies)
                .mapToObj(index -> Technology.builder()
                        .withName(TEST_TECHNOLOGY_NAME + index)
                        .withImageUrl(TEST_TECHNOLOGY_IMAGE_URL + index)
                        .build())
                .collect(Collectors.toList());
    }

    public static Technology buildTechnologyWithEmptyName() {
        return Technology.builder()
                .withName("")
                .withImageUrl(TEST_TECHNOLOGY_IMAGE_URL)
                .build();
    }

    public static Technology buildTechnologyWithNullName() {
        return Technology.builder()
                .withImageUrl(TEST_TECHNOLOGY_IMAGE_URL)
                .build();
    }

    public static Technology buildValidUpdatedTechnology(Technology createdTechnology) {
        return Technology.toBuilder(createdTechnology)
                .withName(TEST_TECHNOLOGY_NAME + UPDATE)
                .withImageUrl(TEST_TECHNOLOGY_IMAGE_URL + UPDATE)
                .build();
    }
}
