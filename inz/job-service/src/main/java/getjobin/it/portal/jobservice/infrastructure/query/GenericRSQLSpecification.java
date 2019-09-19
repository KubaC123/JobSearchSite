package getjobin.it.portal.jobservice.infrastructure.query;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import getjobin.it.portal.jobservice.domain.ManagedEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

public class GenericRSQLSpecification<T extends ManagedEntity> implements Specification<T> {

    private static final char RSQL_WILDCARD = '*';
    private static final char SQL_WILDCARD = '%';

    private String property;
    private ComparisonOperator comparisonOperator;
    private List<String> arguments;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Object> args = castArguments(root);
        Object argument = args.get(0);

        // todo

        switch (RSQLOperator.getSimpleOperator(comparisonOperator)) {
            case EQUAL: {
                if (argument instanceof String) {
                    return criteriaBuilder.like(root.get(property), argument.toString().replace(RSQL_WILDCARD, SQL_WILDCARD));
                }
                if (argument == null) {
                    return criteriaBuilder.isNull(root.get(property));
                }
                return criteriaBuilder.equal(root.get(property), argument);
            }
            case NOT_EQUAL: {
                return null;
            }
        }
        return null;
    }

    private List<Object> castArguments(Root<T> root) {
        Class<? extends Object> propertyType = root.get(property).getJavaType();
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
