package getjobin.it.portal.jobservice.domain.joboffer.control;

import getjobin.it.portal.jobservice.domain.joboffer.entity.JobTechStackRelation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class JobTechStackRelationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Long saveJobTechStackRelation(JobTechStackRelation jobTechStackRelation) {
        entityManager.persist(jobTechStackRelation);
        return jobTechStackRelation.getId();
    }

    public Long updateJobTechStackRelation(JobTechStackRelation jobTechStackRelation) {
        return entityManager.merge(jobTechStackRelation).getId();
    }
}
