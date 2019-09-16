package getjobin.it.portal.jobservice.domain.job.control;

import getjobin.it.portal.jobservice.domain.job.entity.JobTechStackRelation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        return entityManager.createQuery("SELECT relation FROM JobTechStackRelation relation WHERE relation.jobOfferId = :jobId", JobTechStackRelation.class)
                .setParameter("jobId", jobOfferId)
                .getResultList();
    }

    public List<JobTechStackRelation> findByTechStackId(Long techStackId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobTechStackRelation> criteriaQuery = criteriaBuilder.createQuery(JobTechStackRelation.class);
        Root<JobTechStackRelation> jobTechStackRelation = criteriaQuery.from(JobTechStackRelation.class);
        Predicate techStackIdPredicate = criteriaBuilder.equal(jobTechStackRelation.get("techStackId"), techStackId);
        criteriaQuery.where(techStackIdPredicate);
        return entityManager.createQuery(criteriaQuery)
                .getResultList();
    }
}
