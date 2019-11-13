package getjobin.it.portal.jobservice.domain.job.control.enums;

import java.util.Arrays;
import java.util.Optional;

public enum EmploymentType {

    B2B("B2B"),
    EMPLOYMENT("Employment"),
    CONTRACT_AGREEMENT("Contract agreement"),
    CONTRACT_FOR_SPECIFIC_WORK("Contract for specific work"),
    INTERN_AGREEMENT("Intern agreement");

    private String literal;

    private EmploymentType(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public static Optional<EmploymentType> fromString(String employmentType) {
        return Arrays.stream(EmploymentType.values())
                .filter(level -> level.getLiteral().equals(employmentType))
                .findAny();
    }
}
