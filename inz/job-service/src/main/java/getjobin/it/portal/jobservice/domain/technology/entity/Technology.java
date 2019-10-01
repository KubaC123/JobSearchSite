package getjobin.it.portal.jobservice.domain.technology.entity;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import getjobin.it.portal.jobservice.domain.technology.entity.validation.TechnologyUsageValidation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "TECHNOLOGY")
@Getter
@TechnologyUsageValidation(groups = Technology.DeleteValidations.class)
public class Technology extends ManagedEntity {

    public static final String TECHNOLOGY_TYPE = "Technology";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotEmpty(message = "Technology name must be provided")
    private String name;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "JOB_COUNTER")
    @Setter
    private Integer jobCounter;

    public interface DeleteValidations { }

    public static TechnologyEntityBuilder builder() {
        return new TechnologyEntityBuilder();
    }

    public Technology() { }

    public static TechnologyEntityBuilder toBuilder(Technology technology) {
        return new TechnologyEntityBuilder()
                .withId(technology.getId())
                .withName(technology.getName())
                .withImageUrl(technology.getImageUrl());
    }

    public Technology(TechnologyEntityBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.imageUrl = builder.imageUrl;
    }

    public static class TechnologyEntityBuilder {

        private Long id;
        private String name;
        private String imageUrl;

        public TechnologyEntityBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public TechnologyEntityBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public TechnologyEntityBuilder withImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Technology build() {
            return new Technology(this);
        }
    }
}
