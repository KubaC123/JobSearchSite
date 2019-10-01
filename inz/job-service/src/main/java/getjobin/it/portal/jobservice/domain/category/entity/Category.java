package getjobin.it.portal.jobservice.domain.category.entity;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import getjobin.it.portal.jobservice.domain.category.entity.validation.CategoryUsageValidation;
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
@Table(name = "CATEGORY")
@Getter
@CategoryUsageValidation(groups = Category.DeleteValidations.class)
public class Category extends ManagedEntity {

    public static final String CATEGORY_TYPE = "Category";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotEmpty(message = "Category name must be provided")
    private String name;

    @Column(name = "JOB_COUNTER")
    @Setter
    private Integer jobCounter;

    public interface DeleteValidations { }

    public static CategoryEntityBuilder builder() {
        return new CategoryEntityBuilder();
    }

    public Category() { }

    public static CategoryEntityBuilder toBuilder(Category category) {
        return new CategoryEntityBuilder()
                .withId(category.getId())
                .withName(category.getName());
    }

    public Category(CategoryEntityBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }


    public static class CategoryEntityBuilder {

        private Long id;
        private String name;

        public CategoryEntityBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CategoryEntityBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }
}
