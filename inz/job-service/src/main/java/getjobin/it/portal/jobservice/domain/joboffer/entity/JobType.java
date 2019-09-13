package getjobin.it.portal.jobservice.domain.joboffer.entity;

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
}
