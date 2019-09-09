package getjobin.it.portal.jobservice.domain.joboffer.control;

import getjobin.it.portal.jobservice.api.JobTechStackDTO;
import getjobin.it.portal.jobservice.api.TechStackDTO;
import getjobin.it.portal.jobservice.domain.joboffer.entity.JobOffer;
import getjobin.it.portal.jobservice.domain.joboffer.entity.JobTechStackRelation;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobOfferService {

    private Validator validator;
    private JobOfferRepository jobOfferRepository;
    private JobTechStackRelationRepository jobTechStackRelationRepository;
    private TechStackService techStackService;

    @Autowired
    public JobOfferService(Validator validator, JobOfferRepository jobOfferRepository,
                           JobTechStackRelationRepository jobTechStackRelationRepository,
                           TechStackService techStackService) {
        this.validator = validator;
        this.jobOfferRepository = jobOfferRepository;
        this.jobTechStackRelationRepository = jobTechStackRelationRepository;
        this.techStackService = techStackService;
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

    public Long createJobOffer(JobOffer jobOffer, List<JobTechStackDTO> techStackDTOs) {
        techStackService.validateTechStackExistence(getTechStackIds(techStackDTOs));
        validate(jobOffer);
        return jobOfferRepository.saveJobOffer(jobOffer);
    }


    private List<Long> getTechStackIds(List<JobTechStackDTO> jobTechStackDTOs) {
        return jobTechStackDTOs.stream()
                .map(JobTechStackDTO::getTechStack)
                .map(TechStackDTO::getId)
                .collect(Collectors.toList());
    }

    public void createJobOfferTechStackRelations(List<JobTechStackRelation> jobTechStackRelations) {
        jobTechStackRelations.forEach(jobTechStackRelationRepository::saveJobTechStackRelation);
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
