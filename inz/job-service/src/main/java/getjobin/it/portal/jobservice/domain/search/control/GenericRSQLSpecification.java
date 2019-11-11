package getjobin.it.portal.jobservice.domain.search.control;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import getjobin.it.portal.jobservice.domain.ManagedEntity;
import getjobin.it.portal.jobservice.infrastructure.exception.JobServicePreconditions;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class GenericRSQLSpecification<T extends ManagedEntity> implements Specification<T> {

    public static final String RSQL_LOGICAL_OR = ",";
    public static final String RSQL_EQUAL_TO = "==";
    private static final char RSQL_WILDCARD = '*';
    private static final char SQL_WILDCARD = '%';

    private String property;
    private ComparisonOperator comparisonOperator;
    private List<String> arguments;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Expression<String> expression = parseProperty(root);
        List<Object> arguments = castArguments(expression.getJavaType());
        return buildPredicateFromExpression(criteriaBuilder, expression, arguments, arguments.get(0));
    }

    private Expression<String> parseProperty(Root<T> root) {
        Expression<String> expression;
        if(property.contains(".")) {
            String[] nestedProperty = property.split("\\.");
            checkIfPropertyIsSingleNested(nestedProperty);
            Join<Root, Object> join = root.join(nestedProperty[0], JoinType.LEFT);
            expression = join.get(nestedProperty[1]);
        } else {
            expression = root.get(property);
        }
        return expression;
    }

    private void checkIfPropertyIsSingleNested(String[] nestedProperty) {
        JobServicePreconditions.checkArgument(nestedProperty.length == 2,
                MessageFormat.format("Rsql search supports single nested attributes. Provided property: {0} indicates deeper nesting level", String.valueOf(nestedProperty.length)));
    }

    private Predicate buildPredicateFromExpression(CriteriaBuilder criteriaBuilder,
                                                   Expression<String> expression, List<Object> arguments, Object argument) {
        switch (RSQLOperator.getSimpleOperator(comparisonOperator)) {
            case EQUAL: {
                if (argument instanceof String) {
                    return criteriaBuilder.like(expression, argument.toString().replace(RSQL_WILDCARD, SQL_WILDCARD));
                }
                if (argument == null) {
                    return criteriaBuilder.isNull(expression);
                }
                return criteriaBuilder.equal(expression, argument);
            }
            case NOT_EQUAL: {
                if(argument instanceof String) {
                    return criteriaBuilder.notLike(expression, argument.toString().replace(RSQL_WILDCARD, SQL_WILDCARD));
                }
                if(argument == null) {
                    return criteriaBuilder.isNotNull(expression);
                }
                return criteriaBuilder.notEqual(expression, argument);
            }
            case GREATER_THAN: {
                return criteriaBuilder.greaterThan(expression, argument.toString());
            }
            case GREATER_THAN_OR_EQUAL: {
                return criteriaBuilder.greaterThanOrEqualTo(expression, argument.toString());
            }
            case LESS_THAN: {
                return criteriaBuilder.lessThan(expression, argument.toString());
            }
            case LESS_THAN_OR_EQUAL: {
                return criteriaBuilder.lessThanOrEqualTo(expression, argument.toString());
            }
            case IN: {
                return expression.in(arguments);
            }
            case NOT_IN: {
                return criteriaBuilder.not(expression.in(arguments));
            }
        }
        return null;
    }

    private List<Object> castArguments(Class<? extends Object> propertyType) {
        return arguments.stream().map(argument -> {
            if(Integer.class.equals(propertyType)) {
                return Integer.parseInt(argument);
            }
            if(Long.class.equals(propertyType)) {
                return Long.parseLong(argument);
            }
            if(Float.class.equals(propertyType)) {
                return Float.parseFloat(argument);
            }
            if(Boolean.class.equals(propertyType)) {
                return Boolean.parseBoolean(argument);
            }
            return argument;
        }).collect(Collectors.toList());
    }

    public GenericRSQLSpecification(String property, ComparisonOperator comparisonOperator, List<String> arguments) {
        this.property = property;
        this.comparisonOperator = comparisonOperator;
        this.arguments = arguments;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public ComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public void setComparisonOperator(ComparisonOperator comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }
}
