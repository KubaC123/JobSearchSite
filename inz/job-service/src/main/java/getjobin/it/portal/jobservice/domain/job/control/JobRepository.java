package getjobin.it.portal.jobservice.domain.job.control;

import cz.jirutka.rsql.parser.ast.Node;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.job.entity.JobLocationRelation;
import getjobin.it.portal.jobservice.domain.job.entity.JobTechStackRelation;
import getjobin.it.portal.jobservice.domain.search.boundary.ManagedEntityRSQLVisitor;
import getjobin.it.portal.jobservice.domain.search.boundary.QueryService;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.infrastructure.exception.JobServiceException;
import getjobin.it.portal.jobservice.infrastructure.util.CurrentDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Slf4j
public class JobRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private QueryService queryService;

    @Autowired
    private JobTechStackRelationRepository jobTechStackRelationRepository;

    @Autowired
    private JobLocationRelationRepository jobLocationRelationRepository;

    public List<Job> findAll() {
        return queryService.findAll(Job.class);
    }

    public Optional<Job> findById(Long jobId) {
        return Optional.ofNullable(entityManager.find(Job.class, jobId));
    }

    public List<Job> findByIds(List<Long> jobIds) {
        return queryService.queryByIds(jobIds, Job.class);
    }

    public Job getById(Long jobId) {
        return findById(jobId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Job offer with id: {0} does not exist or was removed", String.valueOf(jobId))));
    }

    public List<Job> findByCompany(Company company) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Job> criteriaQuery = criteriaBuilder.createQuery(Job.class);
        Root<Job> job = criteriaQuery.from(Job.class);
        Predicate companyPredicate = JobSpecifications.companySpecification(company).toPredicate(job, criteriaQuery, criteriaBuilder);
        Predicate activePredicate = JobSpecifications.activeSpecification(Boolean.TRUE).toPredicate(job, criteriaQuery, criteriaBuilder);
        criteriaQuery.where(criteriaBuilder.and(activePredicate, companyPredicate));
        TypedQuery<Job> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Job> findByTechnology(Technology technology) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Job> criteriaQuery = criteriaBuilder.createQuery(Job.class);
        Root<Job> job = criteriaQuery.from(Job.class);
        Predicate technologyPredicate = JobSpecifications.technologySpecification(technology).toPredicate(job, criteriaQuery, criteriaBuilder);
        Predicate activePredicate = JobSpecifications.activeSpecification(Boolean.TRUE).toPredicate(job, criteriaQuery, criteriaBuilder);
        criteriaQuery.where(criteriaBuilder.and(activePredicate, technologyPredicate));
        TypedQuery<Job> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Job> findByRSQLNode(Node node) {
        try {
            return findBySpecification(node.accept(new ManagedEntityRSQLVisitor<>()));
        } catch (Exception exception) {
            throw new JobServiceException("Invalid selector in rsql condition.");
        }
    }

    public List<Job> findBySpecification(Specification<Job> specification) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Job> criteriaQuery = criteriaBuilder.createQuery(Job.class);
        Root<Job> job = criteriaQuery.from(Job.class);
        Predicate predicate = specification.toPredicate(job, criteriaQuery, criteriaBuilder);
        criteriaQuery.where(predicate);
        TypedQuery<Job> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public Long save(Job job) {
        job.setCreateDate(CurrentDate.get());
        job.setActive(Boolean.TRUE);
        entityManager.persist(job);
        Long createdJobId = job.getId();
        job.getTechStackRelations().ifPresent(techStackRelations -> createTechStackRelations(createdJobId, techStackRelations));
        job.getLocationRelations().ifPresent(locationRelations -> createLocationRelations(createdJobId, locationRelations));
        return createdJobId;
    }

    public void createTechStackRelations(Long jobId, List<JobTechStackRelation> techStackRelations) {
        techStackRelations.forEach(techStackRelation -> techStackRelation.setJobId(jobId));
        techStackRelations.forEach(jobTechStackRelationRepository::save);
    }

    public void createLocationRelations(Long jobId, List<JobLocationRelation> locationRelations) {
        locationRelations.forEach(locationRelation -> locationRelation.setJobId(jobId));
        locationRelations.forEach(jobLocationRelationRepository::save);
    }

    public Long update(Job job) {
        job.setModifyDate(CurrentDate.get());
        job.getTechStackRelations().ifPresent(techStackRelations -> createTechStackRelations(job.getId(), techStackRelations));
        job.getLocationRelations().ifPresent(locationRelations -> createLocationRelations(job.getId(), locationRelations));
        return entityManager.merge(job).getId();
    }

    public void removeById(Long jobId) {
        findById(jobId).ifPresent(this::remove);
    }

    public void remove(Job job) {
        job.setModifyDate(CurrentDate.get());
        job.setActive(Boolean.FALSE);
        entityManager.merge(job);
    }
}
