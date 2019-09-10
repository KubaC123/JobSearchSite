package getjobin.it.portal.jobservice.domain.joboffer.entity;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
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

@Entity
@Table(name = "JOB_OFFER")
@Getter
public class JobOffer extends ManagedEntity {

    public static final String JOB_OFFER_TYPE = "JobOffer";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TYPE")
    @NotEmpty(message = "Job offer type must be provided")
    private String type;

    @Column(name = "TITLE")
    @NotEmpty(message = "Job offer title must be provided")
    private String title;

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @Column(name = "EXP_LEVEL")
    private String experienceLevel;

    @Column(name = "EMPL_TYPE")
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
    private List<JobTechStackRelation> techStackRelations;

    public JobOffer() { }

    public static JobOfferEntityBuilder builder() {
        return new JobOfferEntityBuilder();
    }

    private JobOffer(JobOfferEntityBuilder builder) {
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

    public static JobOfferEntityBuilder toBuilder(JobOffer jobOffer) {
        return new JobOfferEntityBuilder()
                .withId(jobOffer.getId())
                .withType(jobOffer.getType())
                .withTitle(jobOffer.getTitle())
                .withCompany(jobOffer.getCompany())
                .withExperienceLevel(jobOffer.getExperienceLevel())
                .withEmploymentType(jobOffer.getEmploymentType())
                .withSalaryMin(jobOffer.getSalaryMin())
                .withSalaryMax(jobOffer.getSalaryMax())
                .withCurrency(jobOffer.getCurrency())
                .withDescription(jobOffer.getDescription())
                .withTechnology(jobOffer.getTechnology())
                .withAgreements(jobOffer.getAgreements())
                .withRemote(jobOffer.getRemote())
                .withActive(jobOffer.getActive())
                .withApplications(jobOffer.getApplications())
                .withCreateDate(jobOffer.getCreateDate())
                .withModifyDate(jobOffer.getModifyDate())
                .withExpireDate(jobOffer.getExpireDate())
                .withTechStackRelations(jobOffer.getTechStackRelations());
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

        public JobOfferEntityBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public JobOfferEntityBuilder withType(String type) {
            this.type = type;
            return this;
        }

        public JobOfferEntityBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public JobOfferEntityBuilder withCompany(Company company) {
            this.company = company;
            return this;
        }

        public JobOfferEntityBuilder withExperienceLevel(String experienceLevel) {
            this.experienceLevel = experienceLevel;
            return this;
        }

        public JobOfferEntityBuilder withEmploymentType(String employmentType) {
            this.employmentType = employmentType;
            return this;
        }

        public JobOfferEntityBuilder withSalaryMin(Integer salaryMin) {
            this.salaryMin = salaryMin;
            return this;
        }

        public JobOfferEntityBuilder withSalaryMax(Integer salaryMax) {
            this.salaryMax = salaryMax;
            return this;
        }

        public JobOfferEntityBuilder withCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public JobOfferEntityBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public JobOfferEntityBuilder withTechnology(Technology technology) {
            this.technology = technology;
            return this;
        }

        public JobOfferEntityBuilder withAgreements(String agreements) {
            this.agreements = agreements;
            return this;
        }

        public JobOfferEntityBuilder withRemote(Boolean remote) {
            this.remote = remote;
            return this;
        }

        public JobOfferEntityBuilder withActive(Boolean active) {
            this.active = active;
            return this;
        }

        public JobOfferEntityBuilder withApplications(Integer applications) {
            this.applications = applications;
            return this;
        }

        public JobOfferEntityBuilder withCreateDate(Date createDate) {
            this.createDate = createDate;
            return this;
        }

        public JobOfferEntityBuilder withModifyDate(Date modifyDate) {
            this.modifyDate = modifyDate;
            return this;
        }

        public JobOfferEntityBuilder withExpireDate(Date expireDate) {
            this.expireDate = expireDate;
            return this;
        }

        public JobOfferEntityBuilder withTechStackRelations(List<JobTechStackRelation> techStackRelations) {
            this.techStackRelations = techStackRelations;
            return this;
        }

        public JobOffer build() {
            return new JobOffer(this);
        }
    }
}
