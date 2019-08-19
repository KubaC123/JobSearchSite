package getjobin.it.portal.jobservice.domain.company.control;

import getjobin.it.portal.jobservice.api.CompanyDTO;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.infrastructure.exceptions.JobServiceIllegalArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.text.MessageFormat;
import java.util.List;
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

    public List<Company> findByIds(List<Long> companyIds) {
        return companyRepository.findByIds(companyIds);
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

    public Company getById(Long companyId) {
        return companyRepository.getById(companyId);
    }

    public void removeCompanies(List<Company> companies) {
        companyRepository.removeCompanies(companies);
    }
}
