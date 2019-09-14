package getjobin.it.portal.jobservice.domain.job.control.enums;

import java.util.Arrays;
import java.util.Optional;

public enum JobType {

    FREELANCE("Freelance"),
    REGULAR("Regular");

    private String literal;

    private JobType(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public static Optional<JobType> fromString(String jobType) {
        return Arrays.stream(JobType.values())
                .filter(type -> type.getLiteral().equals(jobType))
                .findAny();
    }
}
