package getjobin.it.portal.jobservice.domain.joboffer.control;

import getjobin.it.portal.jobservice.domain.joboffer.entity.Job;
import getjobin.it.portal.jobservice.domain.joboffer.entity.JobTechStackRelation;
import getjobin.it.portal.jobservice.infrastructure.CurrentDate;
import getjobin.it.portal.jobservice.infrastructure.query.QueryService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JobRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QueryService queryService;
    private JobTechStackRelationRepository jobTechStackRelationRepository;

    @Autowired
    public JobRepository(QueryService queryService, JobTechStackRelationRepository jobTechStackRelationRepository) {
        this.queryService = queryService;
        this.jobTechStackRelationRepository = jobTechStackRelationRepository;
    }

    public Optional<Job> findById(Long jobId) {
        return Optional.ofNullable(entityManager.find(Job.class, jobId));
    }

    public List<Job> findByIds(List<Long> jobIds) {
        return queryService.findEntitiesByIds(Job.class, jobIds);
    }

    public Job getById(Long jobId) {
        return findById(jobId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Job offer with id: {0} does not exist or was removed", String.valueOf(jobId))));
    }

    public Long saveJob(Job job) {
        job.setCreateDate(CurrentDate.get());
        job.setActive(Boolean.TRUE);
        entityManager.persist(job);
        Long createdJobId = job.getId();
        job.getTechStackRelations().ifPresent(jobTechStackRelations -> createJobTechStackRelations(createdJobId, jobTechStackRelations));
        return createdJobId;
    }

    public void createJobTechStackRelations(Long createdJobId, List<JobTechStackRelation> jobTechStackRelations) {
        jobTechStackRelations.forEach(jobTechStackRelation -> jobTechStackRelation.setJobId(createdJobId));
        jobTechStackRelations.forEach(jobTechStackRelationRepository::saveJobTechStackRelation);
    }

    public Long updateJob(Job job) {
        job.setModifyDate(CurrentDate.get());
        job.getTechStackRelations().ifPresent(jobTechStackRelations -> createJobTechStackRelations(job.getId(), jobTechStackRelations));
        return entityManager.merge(job).getId();
    }

    private void updateJobTechStackRelations(Long jobId, List<JobTechStackRelation> jobTechStackRelations) {
        jobTechStackRelations.forEach(jobTechStackRelation -> jobTechStackRelation.setJobId(jobId));
        jobTechStackRelations.forEach(jobTechStackRelationRepository::updateJobTechStackRelation);
    }

    public void removeJobById(Long jobId) {
        findById(jobId).ifPresent(entityManager::remove);
    }

    public void removeJob(Job job) {
        job.setModifyDate(CurrentDate.get());
        job.setActive(Boolean.FALSE);
        entityManager.merge(job);
    }
}
