package getjobin.it.portal.elasticservice.infrastructure.exception;

import java.io.IOException;

@FunctionalInterface
public interface ElasticSearchRequest<T, R> {

    R apply(T t) throws IOException;

    default R unchecked(T t) {
        try {
            return apply(t);
        } catch (Exception ex) {
            throw new ElasticServiceException(ex.getMessage());
            // todo handle ex
        }
    }
}
