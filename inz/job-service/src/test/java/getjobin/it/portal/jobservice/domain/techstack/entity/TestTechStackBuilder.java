package getjobin.it.portal.jobservice.domain.techstack.entity;

import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestTechStackBuilder {

    public static final String TEST_TECH_STACK_NAME = "obieranie ziemniaków";
    private static final String UPDATE = "update";

    public static TechStack buildValidTechStack() {
        return TechStack.builder()
                .withName(TEST_TECH_STACK_NAME)
                .build();
    }

    public static List<TechStack> buildValidTechStacks(int numberOfTechStacks) {
        return IntStream.rangeClosed(0, numberOfTechStacks)
                .mapToObj(nr -> TechStack.builder()
                        .withName(TEST_TECH_STACK_NAME + nr)
                        .build())
                .collect(Collectors.toList());
    }

    public static TechStack buildValidUpdatedTechStack(TechStack foundTechStack) {
        return TechStack.toBuilder(foundTechStack)
                .withName(TEST_TECH_STACK_NAME + UPDATE)
                .build();
    }

    public static TechStack buildTechStackWithEmptyName() {
        return TechStack.builder()
                .withName("")
                .build();
    }

    public static TechStack buildTechStackWithNullName() {
        return TechStack.builder()
                .build();
    }


}
