package getjobin.it.portal.jobservice.domain.joboffer.control;

import getjobin.it.portal.jobservice.domain.joboffer.entity.Job;
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
