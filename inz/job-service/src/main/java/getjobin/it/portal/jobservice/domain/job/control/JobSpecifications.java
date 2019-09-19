package getjobin.it.portal.jobservice.domain.job.control;

import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.job.entity.Job;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecifications {

    public static Specification<Job> companySpecification(Company company) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("company"), company));
    }

    public static Specification<Job> technologySpecification(Technology technology) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("technology"), technology));
    }
}
