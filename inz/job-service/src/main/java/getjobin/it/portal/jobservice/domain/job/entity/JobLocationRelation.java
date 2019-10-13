package getjobin.it.portal.jobservice.domain.job.entity;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JOB_LOCATION_RELATION")
@Getter
public class JobLocationRelation extends ManagedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Setter
    @Column(name = "JOB_ID")
    private Long jobId;

    @Column(name = "LOCATION_ID")
    private Long locationId;

    @Column(name = "REMOTE")
    private Boolean remote;

   public JobLocationRelation() { }

   public static JobLocationRelationBuilder builder() {
       return new JobLocationRelationBuilder();
   }

   private JobLocationRelation(JobLocationRelationBuilder builder) {
       this.id = builder.id;
       this.jobId = builder.jobId;
       this.locationId = builder.locationId;
       this.remote = builder.remote;
   }

   public static JobLocationRelationBuilder toBuilder(JobLocationRelation jobLocationRelation) {
       return new JobLocationRelationBuilder()
               .id(jobLocationRelation.getId())
               .jobId(jobLocationRelation.getJobId())
               .locationId(jobLocationRelation.getLocationId())
               .remote(jobLocationRelation.getRemote());
   }

    public static class JobLocationRelationBuilder {

        private Long id;
        private Long jobId;
        private Long locationId;
        private Boolean remote;

        public JobLocationRelationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public JobLocationRelationBuilder jobId(Long jobId) {
            this.jobId = jobId;
            return this;
        }

        public JobLocationRelationBuilder locationId(Long locationId) {
            this.locationId = locationId;
            return this;
        }

        public JobLocationRelationBuilder remote(Boolean remote) {
            this.remote = remote;
            return this;
        }

        public JobLocationRelation build() {
            return new JobLocationRelation(this);
        }
    }
}
