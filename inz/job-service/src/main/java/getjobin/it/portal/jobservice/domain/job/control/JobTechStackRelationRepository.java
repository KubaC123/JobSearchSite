package getjobin.it.portal.jobservice.domain.job.control;

import getjobin.it.portal.jobservice.domain.job.entity.JobTechStackRelation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

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

    public List<JobTechStackRelation> queryByJobId(Long jobOfferId) {
        return entityManager.createQuery("SELECT relation FROM JOB_TECH_STACK_RELATION relation WHERE relation.JOB_OFFER_ID = :jobId", JobTechStackRelation.class)
                .setParameter("jobId", jobOfferId)
                .getResultList();
    }

    public List<JobTechStackRelation> findByTechStackId(Long techStackId) {
        return entityManager.createQuery("SELECT relation FROM JOB_TECH_STACK_RELATION relation WHERE relation.TECH_STACK_ID = :techStackId", JobTechStackRelation.class)
                .setParameter("techStackId", techStackId)
                .getResultList();
    }
}
