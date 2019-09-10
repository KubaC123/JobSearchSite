package getjobin.it.portal.jobservice.domain.joboffer.control;

import getjobin.it.portal.jobservice.domain.joboffer.entity.JobOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class JobOfferService {

    private Validator validator;
    private JobOfferRepository jobOfferRepository;

    @Autowired
    public JobOfferService(Validator validator, JobOfferRepository jobOfferRepository) {
        this.validator = validator;
        this.jobOfferRepository = jobOfferRepository;
    }

    public Optional<JobOffer> findById(Long jobOfferId) {
        return jobOfferRepository.findById(jobOfferId);
    }

    public List<JobOffer> findByIds(List<Long> jobOfferIds) {
        return jobOfferRepository.findByIds(jobOfferIds);
    }

    public JobOffer getById(Long jobOfferId) {
        return jobOfferRepository.getById(jobOfferId);
    }

    public Long createJobOffer(JobOffer jobOffer) {
        validate(jobOffer);
        return jobOfferRepository.saveJobOffer(jobOffer);
    }

    private void validate(JobOffer jobOffer) {
        Set<ConstraintViolation<JobOffer>>  violations = validator.validate(jobOffer);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Long updateJobOffer(JobOffer jobOffer) {
        validate(jobOffer);
        return jobOfferRepository.updateJobOffer(jobOffer);
    }

    public void removeJobOffer(JobOffer jobOffer) {
        jobOfferRepository.removeJobOffer(jobOffer);
    }
}
