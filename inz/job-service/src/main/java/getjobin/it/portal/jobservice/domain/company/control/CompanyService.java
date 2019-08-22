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

    private Validator validator;
    private CompanyRepository companyRepository;

    @Autowired
    public CompanyService(Validator validator, CompanyRepository companyRepository) {
        this.validator = validator;
        this.companyRepository = companyRepository;
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

    public Long createCompany(Company company) {
        validate(company);
        return companyRepository.saveCompany(company);
    }

    private void validate(Company company) {
        Set<ConstraintViolation<Company>> violations = validator.validate(company);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Long updateCompany(Company company) {
        validate(company);
        return companyRepository.updateCompany(company);
    }


    public void removeCompany(Company company) {
        companyRepository.removeCompany(company);
    }
}
