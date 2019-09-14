package getjobin.it.portal.jobservice.domain.job.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "JOB_TECH_STACK_RELATION")
@Getter
public class JobTechStackRelation {

    private static final String EXP_LEVEL_VALUE_MSG = "Experience level must be in range <1-5>";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Setter
    @Column(name = "JOB_OFFER_ID")
    private Long jobId;

    @Column(name = "TECH_STACK_ID")
    private Long techStackId;

    @Column(name = "EXP_LEVEL")
    @Min(value = 1, message = EXP_LEVEL_VALUE_MSG)
    @Max(value = 5, message = EXP_LEVEL_VALUE_MSG)
    private Integer experienceLevel;

    public JobTechStackRelation() { }

    public static JobTechStackRelationBuilder builder() {
        return new JobTechStackRelationBuilder();
    }

    private JobTechStackRelation(JobTechStackRelationBuilder builder) {
        this.id = builder.id;
        this.jobId = builder.jobId;
        this.techStackId = builder.techStackId;
        this.experienceLevel = builder.experienceLevel;
    }

    public static JobTechStackRelationBuilder toBuilder(JobTechStackRelation jobTechStackRelation) {
        return new JobTechStackRelationBuilder()
                .withId(jobTechStackRelation.getId())
                .withJobId(jobTechStackRelation.getJobId())
                .withTechStackId(jobTechStackRelation.getTechStackId())
                .withExperienceLevel(jobTechStackRelation.getExperienceLevel());
    }

    public static class JobTechStackRelationBuilder {

        private Long id;
        private Long jobId;
        private Long techStackId;
        private Integer experienceLevel;

        public JobTechStackRelationBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public JobTechStackRelationBuilder withJobId(Long jobId) {
            this.jobId = jobId;
            return this;
        }

        public JobTechStackRelationBuilder withTechStackId(Long techStackId) {
            this.techStackId = techStackId;
            return this;
        }

        public JobTechStackRelationBuilder withExperienceLevel(Integer experienceLevel) {
            this.experienceLevel = experienceLevel;
            return this;
        }

        public JobTechStackRelation build() {
            return new JobTechStackRelation(this);
        }
    }
}
