package getjobin.it.portal.jobservice.domain.job.control.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ExperienceLevel {

    JUNIOR("Junior"),
    MID("Mid"),
    SENIOR("Senior");

    private String literal;

    private ExperienceLevel(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public static Optional<ExperienceLevel> fromString(String experienceLevel) {
        return Arrays.stream(ExperienceLevel.values())
                .filter(level -> level.getLiteral().equals(experienceLevel))
                .findAny();
    }
}
