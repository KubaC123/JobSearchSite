package getjobin.it.portal.jobservice.domain.company.control;

import getjobin.it.portal.jobservice.domain.company.entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CompanyService {

    @Autowired
    private Validator validator;

    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Optional<Company> findById(Long companyId) {
        return companyRepository.findById(companyId);
    }

    public List<Company> findByIds(List<Long> companyIds) {
        return companyRepository.findByIds(companyIds);
    }

    public Company getById(Long companyId) {
        return companyRepository.getById(companyId);
    }

    public Long create(Company company) {
        validate(company);
        return companyRepository.save(company);
    }

    private void validate(Company company) {
        Set<ConstraintViolation<Company>> violations = validator.validate(company);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Long update(Company company) {
        validate(company);
        return companyRepository.update(company);
    }

    public void remove(Company company) {
        validateOnRemove(company);
        companyRepository.remove(company);
    }

    private void validateOnRemove(Company company) {
        Set<ConstraintViolation<Company>> violations = validator.validate(company, Company.DeleteValidations.class);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
