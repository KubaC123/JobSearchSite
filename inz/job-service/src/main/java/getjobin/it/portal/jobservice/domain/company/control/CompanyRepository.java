package getjobin.it.portal.jobservice.domain.company.control;

import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.infrastructure.CurrentDate;
import getjobin.it.portal.jobservice.infrastructure.query.QueryService;
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
public class CompanyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QueryService queryService;

    @Autowired
    public CompanyRepository(QueryService queryService) {
        this.queryService = queryService;
    }

    public Optional<Company> findById(Long companyId) {
        return Optional.ofNullable(entityManager.find(Company.class, companyId));
    }

    public List<Company> findByIds(List<Long> companyIds) {
        return queryService.findEntitiesByIds(Company.class, companyIds);
    }

    public Company getById(Long companyId) {
        return findById(companyId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Company with id: {0} does not exist or was removed", String.valueOf(companyId))));
    }

    public Long saveCompany(Company company) {
        company.setCreateDate(CurrentDate.get());
        entityManager.persist(company);
        return company.getId();
    }

    public Long updateCompany(Company company) {
        company.setModifyDate(CurrentDate.get());
        return entityManager.merge(company).getId();
    }

    public void removeCompanyById(Long companyId) {
        findById(companyId).ifPresent(entityManager::remove);
    }

    public void removeCompany(Company company) {
        entityManager.remove(company);
    }
}