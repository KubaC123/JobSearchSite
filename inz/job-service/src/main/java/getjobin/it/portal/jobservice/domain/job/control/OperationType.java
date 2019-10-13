package getjobin.it.portal.jobservice.domain.job.control;

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
