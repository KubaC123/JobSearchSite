package getjobin.it.portal.jobservice.domain.job.boundary;

public enum OperationType {

    CREATE("create"),
    UPDATE("update"),
    DELETE("delete");

    private String literal;

    OperationType(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }
}
