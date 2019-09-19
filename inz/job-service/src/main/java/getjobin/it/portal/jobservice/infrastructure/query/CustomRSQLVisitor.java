package getjobin.it.portal.jobservice.infrastructure.query;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import getjobin.it.portal.jobservice.domain.ManagedEntity;
import org.springframework.data.jpa.domain.Specification;

public class CustomRSQLVisitor<T extends ManagedEntity> implements RSQLVisitor<Specification<T>, Void> {

    private GenericRSQLSpecificationBuilder<T> builder;

    @Override
    public Specification<T> visit(AndNode andNode, Void aVoid) {
        return builder.createSpecification(andNode);
    }

    @Override
    public Specification<T> visit(OrNode orNode, Void aVoid) {
        return builder.createSpecification(orNode);
    }

    @Override
    public Specification<T> visit(ComparisonNode comparisonNode, Void aVoid) {
    return builder.createSpecification(comparisonNode);
    }
}
