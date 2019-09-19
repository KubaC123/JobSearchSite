package getjobin.it.portal.jobservice.infrastructure.query;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;
import getjobin.it.portal.jobservice.domain.ManagedEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GenericRSQLSpecificationBuilder<T extends ManagedEntity> {

    public Specification<T> createSpecification(Node node) {
        if(node instanceof LogicalNode) {
            return createSpecification((LogicalNode) node);
        }
        if(node instanceof ComparisonNode) {
            return createSpecification((ComparisonNode) node);
        }
        return null;
    }

    public Specification<T> createSpecification(LogicalNode logicalNode) {
        List<Specification<T>> specifications = logicalNode.getChildren().stream()
                .map(this::createSpecification)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Specification<T> result = specifications.get(0);
        if(LogicalOperator.AND.equals(logicalNode.getOperator())) {
            specifications.forEach(specification -> Specification.where(result).and(specification));
        }
        if(LogicalOperator.OR.equals(logicalNode.getOperator())) {
            specifications.forEach(specification -> Specification.where(result).or(specification));
        }
        return result;
    }

    public Specification<T> createSpecification(ComparisonNode comparisonNode) {
        return Specification.where(
                new GenericRSQLSpecification<>(
                        comparisonNode.getSelector(),
                        comparisonNode.getOperator(),
                        comparisonNode.getArguments()
                )
        );
    }
}
