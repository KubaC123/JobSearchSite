package getjobin.it.portal.jobservice.domain.job.entity;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.job.entity.validation.CategoryValidation;
import getjobin.it.portal.jobservice.domain.job.entity.validation.CompanyValidation;
import getjobin.it.portal.jobservice.domain.job.entity.validation.EmploymentTypeValidation;
import getjobin.it.portal.jobservice.domain.job.entity.validation.ExperienceLevelValidation;
import getjobin.it.portal.jobservice.domain.job.entity.validation.JobTypeValidation;
import getjobin.it.portal.jobservice.domain.job.entity.validation.LocationValidation;
import getjobin.it.portal.jobservice.domain.job.entity.validation.TechStacksValidation;
import getjobin.it.portal.jobservice.domain.job.entity.validation.TechnologyValidation;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Entity
@Table(name = "JOB_OFFER")
@Getter
public class Job extends ManagedEntity {

    public static final String JOB_TYPE = "Job";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TYPE")
    @JobTypeValidation
    private String type;

    @Column(name = "TITLE")
    @NotEmpty(message = "Title must be provided")
    private String title;

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID")
    @CompanyValidation
    private Company company;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    @CategoryValidation
    private Category category;

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

    @Column(name = "START_DATE")
    private String startDate;

    @Column(name = "CONTRACT_DURATION")
    private String contractDuration;

    @Column(name = "FLEXIBLE_WORK_HOURS")
    private Boolean flexibleWorkHours;

    @Column(name = "CURRENCY")
    private String currency;
    
    @Column(name = "DESCRIPTION")
    @NotEmpty(message = "Description must be provided")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "TECHNOLOGY_ID")
    @TechnologyValidation
    private Technology technology;

    @Column(name = "PROJECT_INDUSTRY")
    private String projectIndustry;

    @Column(name = "PROJECT_TEAM_SIZE")
    private Integer projectTeamSize;

    @Column(name = "PROJECT_DESCRIPTION")
    private String projectDescription;

    @Column(name = "DEVELOPMENT")
    private Integer development;

    @Column(name = "TESTING")
    private Integer testing;

    @Column(name = "MAINTENANCE")
    private Integer maintenance;

    @Column(name = "CLIENT_SUPPORT")
    private Integer clientSupport;

    @Column(name = "MEETINGS")
    private Integer meetings;

    @Column(name = "LEADINGS")
    private Integer leading;
    
    @Column(name = "DOCUMENTATION")
    private Integer documentation;

    @Column(name = "OTHER_ACTIVITIES")
    private Integer otherActivities;

    @Column(name = "AGREEMENTS")
    private String agreements;

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

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "JOB_ID")
    @TechStacksValidation
    private List<JobTechStackRelation> techStackRelations;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "JOB_ID")
    @LocationValidation
    private List<JobLocationRelation> locationRelations;

    public Optional<List<JobTechStackRelation>> getTechStackRelations() {
        return Optional.ofNullable(techStackRelations);
    }

    public Optional<List<JobLocationRelation>> getLocationRelations() {
        return Optional.ofNullable(locationRelations);
    }

    public Job() { }

    public static JobEntityBuilder builder() {
        return new JobEntityBuilder();
    }

    private Job(JobEntityBuilder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.title = builder.title;
        this.company = builder.company;
        this.category = builder.category;
        this.experienceLevel = builder.experienceLevel;
        this.employmentType = builder.employmentType;
        this.salaryMin = builder.salaryMin;
        this.salaryMax = builder.salaryMax;
        this.startDate = builder.startDate;
        this.contractDuration = builder.contractDuration;
        this.flexibleWorkHours = builder.flexibleWorkHours;
        this.currency = builder.currency;
        this.description = builder.description;
        this.technology = builder.technology;
        this.projectIndustry = builder.projectIndustry;
        this.projectTeamSize = builder.projectTeamSize;
        this.projectDescription = builder.projectDescription;
        this.development = builder.development;
        this.testing = builder.testing;
        this.maintenance = builder.maintenance;
        this.clientSupport = builder.clientSupport;
        this.meetings = builder.meetings;
        this.leading = builder.leading;
        this.documentation = builder.documentation;
        this.otherActivities = builder.otherActivities;
        this.agreements = builder.agreements;
        this.active = builder.active;
        this.applications = builder.applications;
        this.createDate = builder.createDate;
        this.modifyDate = builder.modifyDate;
        this.expireDate = builder.expireDate;
        this.techStackRelations = builder.techStackRelations;
        this.locationRelations = builder.locationRelations;
    }

    public static JobEntityBuilder toBuilder(Job job) {
        return new JobEntityBuilder()
                .id(job.getId())
                .type(job.getType())
                .title(job.getTitle())
                .company(job.getCompany())
                .category(job.getCategory())
                .experienceLevel(job.getExperienceLevel())
                .employmentType(job.getEmploymentType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .startDate(job.getStartDate())
                .contractDuration(job.getContractDuration())
                .flexibleWorkHours(job.getFlexibleWorkHours())
                .currency(job.getCurrency())
                .description(job.getDescription())
                .technology(job.getTechnology())
                .projectIndustry(job.getProjectIndustry())
                .projectTeamSize(job.getProjectTeamSize())
                .projectDescription(job.getProjectDescription())
                .development(job.getDevelopment())
                .testing(job.getTesting())
                .maintenance(job.getMaintenance())
                .clientSupport(job.getClientSupport())
                .meetings(job.getMeetings())
                .leading(job.getLeading())
                .documentation(job.getDocumentation())
                .otherActivities(job.getOtherActivities())
                .agreements(job.getAgreements())
                .active(job.getActive())
                .applications(job.getApplications())
                .createDate(job.getCreateDate())
                .modifyDate(job.getModifyDate())
                .expireDate(job.getExpireDate())
                .techStackRelations(job.getTechStackRelations().orElse(null))
                .locationRelations(job.getLocationRelations().orElse(null));
    }

    public static class JobEntityBuilder {

        private Long id;
        private String type;
        private String title;
        private Company company;
        private Category category;
        private String experienceLevel;
        private String employmentType;
        private Integer salaryMin;
        private Integer salaryMax;
        private String startDate;
        private String contractDuration;
        private Boolean flexibleWorkHours;
        private String currency;
        private String description;
        private Technology technology;
        private String projectIndustry;
        private Integer projectTeamSize;
        private String projectDescription;
        private Integer development;
        private Integer testing;
        private Integer maintenance;
        private Integer clientSupport;
        private Integer meetings;
        private Integer leading;
        private Integer documentation;
        private Integer otherActivities;
        private String agreements;
        private Boolean active;
        private Integer applications;
        private Date createDate;
        private Date modifyDate;
        private Date expireDate;
        private List<JobTechStackRelation> techStackRelations;
        private List<JobLocationRelation> locationRelations;

        public JobEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public JobEntityBuilder type(String type) {
            this.type = type;
            return this;
        }

        public JobEntityBuilder title(String title) {
            this.title = title;
            return this;
        }

        public JobEntityBuilder company(Company company) {
            this.company = company;
            return this;
        }

        public JobEntityBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public JobEntityBuilder experienceLevel(String experienceLevel) {
            this.experienceLevel = experienceLevel;
            return this;
        }

        public JobEntityBuilder employmentType(String employmentType) {
            this.employmentType = employmentType;
            return this;
        }

        public JobEntityBuilder salaryMin(Integer salaryMin) {
            this.salaryMin = salaryMin;
            return this;
        }

        public JobEntityBuilder salaryMax(Integer salaryMax) {
            this.salaryMax = salaryMax;
            return this;
        }

        public JobEntityBuilder startDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public JobEntityBuilder contractDuration(String contractDuration) {
            this.contractDuration = contractDuration;
            return this;
        }

        public JobEntityBuilder flexibleWorkHours(Boolean flexibleWorkHours) {
            this.flexibleWorkHours = flexibleWorkHours;
            return this;
        }

        public JobEntityBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public JobEntityBuilder description(String description) {
            this.description = description;
            return this;
        }

        public JobEntityBuilder technology(Technology technology) {
            this.technology = technology;
            return this;
        }

        public JobEntityBuilder projectIndustry(String projectIndustry) {
            this.projectIndustry = projectIndustry;
            return this;
        }

        public JobEntityBuilder projectTeamSize(Integer projectTeamSize) {
            this.projectTeamSize = projectTeamSize;
            return this;
        }

        public JobEntityBuilder projectDescription(String projectDescription) {
            this.projectDescription = projectDescription;
            return this;
        }

        public JobEntityBuilder development(Integer development) {
            this.development = development;
            return this;
        }

        public JobEntityBuilder testing(Integer testing) {
            this.testing = testing;
            return this;
        }

        public JobEntityBuilder maintenance(Integer maintenance) {
            this.maintenance = maintenance;
            return this;
        }

        public JobEntityBuilder clientSupport(Integer clientSupport) {
            this.clientSupport = clientSupport;
            return this;
        }

        public JobEntityBuilder meetings(Integer meetings) {
            this.meetings = meetings;
            return this;
        }

        public JobEntityBuilder leading(Integer leading) {
            this.leading = leading;
            return this;
        }

        public JobEntityBuilder documentation(Integer documentation) {
            this.documentation = documentation;
            return this;
        }

        public JobEntityBuilder otherActivities(Integer otherActivities) {
            this.otherActivities = otherActivities;
            return this;
        }


        public JobEntityBuilder agreements(String agreements) {
            this.agreements = agreements;
            return this;
        }

        public JobEntityBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public JobEntityBuilder applications(Integer applications) {
            this.applications = applications;
            return this;
        }

        public JobEntityBuilder createDate(Date createDate) {
            this.createDate = createDate;
            return this;
        }

        public JobEntityBuilder modifyDate(Date modifyDate) {
            this.modifyDate = modifyDate;
            return this;
        }

        public JobEntityBuilder expireDate(Date expireDate) {
            this.expireDate = expireDate;
            return this;
        }

        public JobEntityBuilder techStackRelations(List<JobTechStackRelation> techStackRelations) {
            this.techStackRelations = techStackRelations;
            return this;
        }

        public JobEntityBuilder locationRelations(List<JobLocationRelation> locationRelations) {
            this.locationRelations = locationRelations;
            return this;
        }

        public Job build() {
            if(otherActivities == null) {
                otherActivities = getOtherActivities();
            }
            return new Job(this);
        }

        private Integer getOtherActivities() {
            Integer usedPercent = Stream.of(development, testing, maintenance,
                    clientSupport, meetings, leading, documentation)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum();
            return 100 - usedPercent;
        }
    }
}
