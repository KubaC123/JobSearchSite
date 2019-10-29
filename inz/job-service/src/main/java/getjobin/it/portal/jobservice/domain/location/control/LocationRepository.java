package getjobin.it.portal.jobservice.domain.location.control;

import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.domain.search.boundary.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class LocationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private QueryService queryService;

    public List<Location> findAll() {
        return queryService.findAll(Location.class);
    }

    public Optional<Location> findById(Long locationId) {
        return Optional.ofNullable(entityManager.find(Location.class, locationId));
    }

    public List<Location> findByIds(List<Long> locationIds) {
        return queryService.queryByIds(locationIds, Location.class);
    }

    public Location getById(Long locationId) {
        return findById(locationId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Location with id: {0} does not exist or was removed", String.valueOf(locationId))));
    }

    public Long save(Location location) {
        entityManager.persist(location);
        return location.getId();
    }

    public Long update(Location location) {
        return entityManager.merge(location).getId();
    }

    public void removeById(Long locationId) {
        findById(locationId).ifPresent(entityManager::remove);
    }

    public void remove(Location location) {
        entityManager.remove(location);
    }
}
