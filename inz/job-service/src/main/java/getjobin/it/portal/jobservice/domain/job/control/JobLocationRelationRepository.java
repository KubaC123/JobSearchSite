package getjobin.it.portal.jobservice.domain.job.control;

import getjobin.it.portal.jobservice.domain.job.entity.JobLocationRelation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class JobLocationRelationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Long save(JobLocationRelation jobLocationRelation) {
        entityManager.persist(jobLocationRelation);
        return jobLocationRelation.getId();
    }

    public Long update(JobLocationRelation jobLocationRelation) {
        return entityManager.merge(jobLocationRelation).getId();
    }
}
