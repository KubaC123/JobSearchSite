package getjobin.it.portal.jobservice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "COMPANY")
@Getter
public class CompanyEntity extends ManagedEntity {

    public static final String COMPANY_TYPE = "Company";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME")
    @NotNull(message = "Company name must be provided")
    private String name;

    @Column(name = "WEBSITE")
    private String webSite;

    @Column(name = "SIZE")
    private String size;

    @Column(name = "LOGO_PATH")
    private String logoPath;

    @Setter
    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Setter
    @Column(name = "MODIFY_DATE")
    private Date modifyDate;

    public static CompanyEntityBuilder builder() {
        return new CompanyEntityBuilder();
    }

    public CompanyEntity() { }

    public static CompanyEntityBuilder toBuilder(CompanyEntity companyEntity) {
        return new CompanyEntityBuilder()
                .withId(companyEntity.getId())
                .withName(companyEntity.getName())
                .withWebSite(companyEntity.getWebSite())
                .withSize(companyEntity.getSize())
                .withLogoPath(companyEntity.getLogoPath())
                .withCreateDate(companyEntity.getCreateDate())
                .withModifyDate(companyEntity.getModifyDate());
    }

    private CompanyEntity(CompanyEntityBuilder builder) {
        this.id = builder.id;
        this.name= builder.name;
        this.webSite = builder.webSite;
        this.size = builder.size;
        this.logoPath = builder.logoPath;
        this.createDate = builder.createDate;
        this.modifyDate = builder.modifyDate;
    }

    public static class CompanyEntityBuilder {

        private Long id;
        private String name;
        private String webSite;
        private String size;
        private String logoPath;
        private Date createDate;
        private Date modifyDate;

        public CompanyEntityBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CompanyEntityBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CompanyEntityBuilder withWebSite(String webSite) {
            this.webSite = webSite;
            return this;
        }

        public CompanyEntityBuilder withSize(String size) {
            this.size = size;
            return this;
        }

        public CompanyEntityBuilder withLogoPath(String logoPath) {
            this.logoPath = logoPath;
            return this;
        }

        public CompanyEntityBuilder withCreateDate(Date createDate) {
            this.createDate = createDate;
            return this;
        }

        public CompanyEntityBuilder withModifyDate(Date modifyDate) {
            this.modifyDate = modifyDate;
            return this;
        }

        public CompanyEntity build() {
            return new CompanyEntity(this);
        }
    }
}
