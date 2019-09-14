package getjobin.it.portal.jobservice.domain.techstack.entity;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import getjobin.it.portal.jobservice.domain.techstack.entity.validation.TechStackUsageValidation;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "TECH_STACK")
@Getter
@TechStackUsageValidation
public class TechStack extends ManagedEntity {

    public static String TECH_STACK_TYPE = "TechStack";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    @NotEmpty(message = "Tech stack name must be provided")
    private String name;

    public static TechStackBuilder builder() {
        return new TechStackBuilder();
    }

    public TechStack() { }

    public static TechStackBuilder toBuilder(TechStack techStack) {
        return new TechStackBuilder()
                .withId(techStack.getId())
                .withName(techStack.getName());
    }

    private TechStack(TechStackBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    public static class TechStackBuilder {

        private Long id;
        private String name;

        public TechStackBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public TechStackBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public TechStack build() {
            return new TechStack(this);
        }
    }
}
