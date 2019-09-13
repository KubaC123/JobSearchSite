package getjobin.it.portal.jobservice.domain.joboffer.entity;

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
}
