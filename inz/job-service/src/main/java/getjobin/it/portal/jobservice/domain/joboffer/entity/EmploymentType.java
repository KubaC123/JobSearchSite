package getjobin.it.portal.jobservice.domain.joboffer.entity;

public enum EmploymentType {

    B2B("B2B"),
    EMP_CONTRACT("EmploymentContract"),
    FEE_FOR_TASK("FeeForTask"),
    TASK_CONTRACT("TaskContract");

    private String literal;

    private EmploymentType(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }
}
