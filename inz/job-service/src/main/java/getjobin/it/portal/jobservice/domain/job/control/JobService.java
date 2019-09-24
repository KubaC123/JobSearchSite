package getjobin.it.portal.jobservice.domain.job.control;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.ast.Node;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.infrastructure.exceptions.JobServiceIllegalArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class JobService {

    private Validator validator;
    private JobRepository jobRepository;

    @Autowired
    public JobService(Validator validator, JobRepository jobRepository) {
        this.validator = validator;
        this.jobRepository = jobRepository;
    }

    public Optional<Job> findById(Long jobId) {
        return jobRepository.findById(jobId);
    }

    public List<Job> findByIds(List<Long> jobIds) {
        return jobRepository.findByIds(jobIds);
    }

    public Job getById(Long jobId) {
        return jobRepository.getById(jobId);
    }

    public List<Job> findByCompany(Company company) {
        return jobRepository.findByCompany(company);
    }

    public List<Job> findByTechnology(Technology technology) {
        return jobRepository.findByTechnology(technology);
    }

    public List<Job> findByRSQLCondition(String rsqlCondition) {
        Node node = tryParseRSQLCondition(rsqlCondition);
        return jobRepository.findByRSQLNode(node);
    }

    private Node tryParseRSQLCondition(String rsqlCondition) {
        try {
            return new RSQLParser().parse(rsqlCondition);
        } catch (RSQLParserException exception) {
            throw new JobServiceIllegalArgumentException("Invalid rsql syntax. Please refer to swagger documentation for valid operators.");
        }
    }

    public Long createJob(Job job) {
        validate(job);
        return jobRepository.saveJob(job);
    }

    private void validate(Job job) {
        Set<ConstraintViolation<Job>>  violations = validator.validate(job);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Long updateJob(Job job) {
        validate(job);
        return jobRepository.updateJob(job);
    }

    public void removeJob(Job job) {
        jobRepository.removeJob(job);
    }
}
