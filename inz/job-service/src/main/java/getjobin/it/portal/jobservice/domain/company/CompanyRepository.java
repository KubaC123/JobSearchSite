package getjobin.it.portal.jobservice.domain.company;

import getjobin.it.portal.jobservice.domain.CompanyEntity;
import getjobin.it.portal.jobservice.infrastructure.CurrentDate;
import getjobin.it.portal.jobservice.infrastructure.query.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class CompanyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private Validator validator;
    private QueryService queryService;

    @Autowired
    public CompanyRepository(Validator validator, QueryService queryService) {
        this.validator = validator;
        this.queryService = queryService;
    }

    public List<CompanyEntity> findByIds(List<Long> companyIds) {
        return queryService.findEntitiesByIds(CompanyEntity.class, companyIds);
    }

    public CompanyEntity findById(Long companyId) {
        return entityManager.find(CompanyEntity.class, companyId);
    }

    @Transactional
    public Long createCompany(CompanyEntity company) {
        validate(company);
        return saveCompany(company);
    }

    private void validate(CompanyEntity company) {
        Set<ConstraintViolation<CompanyEntity>> violations = validator.validate(company);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private Long saveCompany(CompanyEntity company) {
        company.setCreateDate(CurrentDate.get());
        entityManager.persist(company);
        return company.getId();
    }

    @Transactional
    public Long updateCompany(CompanyEntity company) {
        validate(company);
        return entityManager.merge(company).getId();
    }

    @Transactional
    public void removeCompanyById(Long companyId) {
        Optional.ofNullable(findById(companyId))
                .ifPresent(entityManager::remove);
    }

    public void removeCompanies(List<CompanyEntity> companies) {
        log.info(MessageFormat.format("[COMPANY] removing companies with ids: {0}", getCommaSeparatedIds(companies)));
        companies.forEach(this::removeCompany);
    }

    private String getCommaSeparatedIds(List<CompanyEntity> companies) {
        return companies.stream()
                .map(CompanyEntity::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Transactional
    public void removeCompany(CompanyEntity company) {
        entityManager.remove(company);
    }
}
