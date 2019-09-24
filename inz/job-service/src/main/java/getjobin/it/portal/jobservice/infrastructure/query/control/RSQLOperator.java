package getjobin.it.portal.jobservice.infrastructure.query.control;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

import java.util.Arrays;

public enum RSQLOperator {

    EQUAL(RSQLOperators.EQUAL),
    NOT_EQUAL(RSQLOperators.NOT_EQUAL),
    GREATER_THAN(RSQLOperators.GREATER_THAN),
    GREATER_THAN_OR_EQUAL(RSQLOperators.GREATER_THAN_OR_EQUAL),
    LESS_THAN(RSQLOperators.LESS_THAN),
    LESS_THAN_OR_EQUAL(RSQLOperators.LESS_THAN_OR_EQUAL),
    IN(RSQLOperators.IN),
    NOT_IN(RSQLOperators.NOT_IN);

    private ComparisonOperator operator;

    RSQLOperator(ComparisonOperator operator) {
        this.operator = operator;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    public static RSQLOperator getSimpleOperator(ComparisonOperator operator) {
        return Arrays.stream(RSQLOperator.values())
                .filter(value -> value.getOperator().equals(operator))
                .findAny()
                .orElse(null);
    }
}
