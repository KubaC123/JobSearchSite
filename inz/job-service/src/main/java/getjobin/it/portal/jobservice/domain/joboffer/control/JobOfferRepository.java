package getjobin.it.portal.jobservice.domain.joboffer.control;

import getjobin.it.portal.jobservice.domain.joboffer.entity.JobOffer;
import getjobin.it.portal.jobservice.infrastructure.CurrentDate;
import getjobin.it.portal.jobservice.infrastructure.query.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JobOfferRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QueryService queryService;

    @Autowired
    public JobOfferRepository(QueryService queryService) {
        this.queryService = queryService;
    }

    public Optional<JobOffer> findById(Long jobOfferId) {
        return Optional.ofNullable(entityManager.find(JobOffer.class, jobOfferId));
    }

    public List<JobOffer> findByIds(List<Long> jobOfferIds) {
        return queryService.findEntitiesByIds(JobOffer.class, jobOfferIds);
    }

    public JobOffer getById(Long jobOfferId) {
        return findById(jobOfferId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Job offer with id: {0} does not exist or was removed", String.valueOf(jobOfferId))));
    }

    public Long saveJobOffer(JobOffer jobOffer) {
        jobOffer.setCreateDate(CurrentDate.get());
        entityManager.persist(jobOffer);
        return jobOffer.getId();
    }

    public Long updateJobOffer(JobOffer jobOffer) {
        jobOffer.setModifyDate(CurrentDate.get());
        return entityManager.merge(jobOffer).getId();
    }

    public void removeJobOfferById(Long jobOfferId) {
        findById(jobOfferId).ifPresent(entityManager::remove);
    }

    public void removeJobOffer(JobOffer jobOffer) {
        entityManager.remove(jobOffer);
    }
}
