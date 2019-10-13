package getjobin.it.portal.jobservice.domain.search.boundary;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import getjobin.it.portal.jobservice.domain.ManagedEntity;
import getjobin.it.portal.jobservice.domain.search.control.GenericRSQLSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;

public class ManagedEntityRSQLVisitor<T extends ManagedEntity> implements RSQLVisitor<Specification<T>, Void> {

    private GenericRSQLSpecificationBuilder<T> builder;

    public ManagedEntityRSQLVisitor() {
        builder = new GenericRSQLSpecificationBuilder<>();
    }

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
