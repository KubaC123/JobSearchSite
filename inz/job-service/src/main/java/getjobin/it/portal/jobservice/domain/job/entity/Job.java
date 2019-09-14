package getjobin.it.portal.jobservice.domain.job.entity;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.job.entity.validation.EmploymentTypeValidation;
import getjobin.it.portal.jobservice.domain.job.entity.validation.ExperienceLevelValidation;
import getjobin.it.portal.jobservice.domain.job.entity.validation.JobTypeValidation;
import getjobin.it.portal.jobservice.domain.job.entity.validation.TechStacksValidation;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "JOB_OFFER")
@Getter
public class Job extends ManagedEntity {

    public static final String JOB_OFFER_TYPE = "Job";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TYPE")
    @NotEmpty(message = "Job type must be provided")
    @JobTypeValidation
    private String type;

    @Column(name = "TITLE")
    @NotEmpty(message = "Job title must be provided")
    private String title;

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @Column(name = "EXP_LEVEL")
    @ExperienceLevelValidation
    private String experienceLevel;

    @Column(name = "EMPL_TYPE")
    @EmploymentTypeValidation
    private String employmentType;

    @Column(name = "SALARY_MIN")
    private Integer salaryMin;

    @Column(name = "SALARY_MAX")
    private Integer salaryMax;

    @Column(name = "CURRENCY")
    private String currency;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "TECHNOLOGY_ID")
    private Technology technology;
    
    @Column(name = "AGREEMENTS")
    private String agreements;
    
    @Column(name = "REMOTE")
    private Boolean remote;

    @Setter
    @Column(name = "ACTIVE")
    private Boolean active;

    @Column(name = "Applications")
    private Integer applications;

    @Setter
    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Setter
    @Column(name = "MODIFY_DATE")
    private Date modifyDate;

    @Column(name = "EXPIRE_DATE")
    private Date expireDate;

    @OneToMany
    @JoinColumn(name = "JOB_OFFER_ID")
    @TechStacksValidation
    private List<JobTechStackRelation> techStackRelations;

    public Optional<List<JobTechStackRelation>> getTechStackRelations() {
        return Optional.ofNullable(techStackRelations);
    }

    public Job() { }

    public static JobOfferEntityBuilder builder() {
        return new JobOfferEntityBuilder();
    }

    private Job(JobOfferEntityBuilder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.title = builder.title;
        this.company = builder.company;
        this.experienceLevel = builder.experienceLevel;
        this.employmentType = builder.employmentType;
        this.salaryMin = builder.salaryMin;
        this.salaryMax = builder.salaryMax;
        this.currency = builder.currency;
        this.description = builder.description;
        this.technology = builder.technology;
        this.agreements = builder.agreements;
        this.remote = builder.remote;
        this.active = builder.active;
        this.applications = builder.applications;
        this.createDate = builder.createDate;
        this.modifyDate = builder.modifyDate;
        this.expireDate = builder.expireDate;
        this.techStackRelations = builder.techStackRelations;
    }

    public static JobOfferEntityBuilder toBuilder(Job job) {
        return new JobOfferEntityBuilder()
                .id(job.getId())
                .type(job.getType())
                .title(job.getTitle())
                .company(job.getCompany())
                .experienceLevel(job.getExperienceLevel())
                .employmentType(job.getEmploymentType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .currency(job.getCurrency())
                .description(job.getDescription())
                .technology(job.getTechnology())
                .agreements(job.getAgreements())
                .remote(job.getRemote())
                .active(job.getActive())
                .applications(job.getApplications())
                .createDate(job.getCreateDate())
                .modifyDate(job.getModifyDate())
                .expireDate(job.getExpireDate())
                .techStackRelations(job.getTechStackRelations().orElse(null));
    }

    public static class JobOfferEntityBuilder {

        private Long id;
        private String type;
        private String title;
        private Company company;
        private String experienceLevel;
        private String employmentType;
        private Integer salaryMin;
        private Integer salaryMax;
        private String currency;
        private String description;
        private Technology technology;
        private String agreements;
        private Boolean remote;
        private Boolean active;
        private Integer applications;
        private Date createDate;
        private Date modifyDate;
        private Date expireDate;
        private List<JobTechStackRelation> techStackRelations;

        public JobOfferEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public JobOfferEntityBuilder type(String type) {
            this.type = type;
            return this;
        }

        public JobOfferEntityBuilder title(String title) {
            this.title = title;
            return this;
        }

        public JobOfferEntityBuilder company(Company company) {
            this.company = company;
            return this;
        }

        public JobOfferEntityBuilder experienceLevel(String experienceLevel) {
            this.experienceLevel = experienceLevel;
            return this;
        }

        public JobOfferEntityBuilder employmentType(String employmentType) {
            this.employmentType = employmentType;
            return this;
        }

        public JobOfferEntityBuilder salaryMin(Integer salaryMin) {
            this.salaryMin = salaryMin;
            return this;
        }

        public JobOfferEntityBuilder salaryMax(Integer salaryMax) {
            this.salaryMax = salaryMax;
            return this;
        }

        public JobOfferEntityBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public JobOfferEntityBuilder description(String description) {
            this.description = description;
            return this;
        }

        public JobOfferEntityBuilder technology(Technology technology) {
            this.technology = technology;
            return this;
        }

        public JobOfferEntityBuilder agreements(String agreements) {
            this.agreements = agreements;
            return this;
        }

        public JobOfferEntityBuilder remote(Boolean remote) {
            this.remote = remote;
            return this;
        }

        public JobOfferEntityBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public JobOfferEntityBuilder applications(Integer applications) {
            this.applications = applications;
            return this;
        }

        public JobOfferEntityBuilder createDate(Date createDate) {
            this.createDate = createDate;
            return this;
        }

        public JobOfferEntityBuilder modifyDate(Date modifyDate) {
            this.modifyDate = modifyDate;
            return this;
        }

        public JobOfferEntityBuilder expireDate(Date expireDate) {
            this.expireDate = expireDate;
            return this;
        }

        public JobOfferEntityBuilder techStackRelations(List<JobTechStackRelation> techStackRelations) {
            this.techStackRelations = techStackRelations;
            return this;
        }

        public Job build() {
            return new Job(this);
        }
    }
}
