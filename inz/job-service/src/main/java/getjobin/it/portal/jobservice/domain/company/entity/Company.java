package getjobin.it.portal.jobservice.domain.company.entity;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "COMPANY")
@Getter
public class Company extends ManagedEntity {

    public static final String COMPANY_TYPE = "Company";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME")
    @NotEmpty(message = "Company name must be provided")
    private String name;

    @Column(name = "WEBSITE")
    private String webSite;

    @Column(name = "SIZE")
    private String size;

    @Column(name = "LOGO")
    private String logoPath;

    @Column(name = "ESTABLISHMENT")
    private Integer establishment;

    @Column(name = "DESCRIPTION")
    private String description;

    @Setter
    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Setter
    @Column(name = "MODIFY_DATE")
    private Date modifyDate;

    public static CompanyEntityBuilder builder() {
        return new CompanyEntityBuilder();
    }

    public Company() { }

    private Company(CompanyEntityBuilder builder) {
        this.id = builder.id;
        this.name= builder.name;
        this.webSite = builder.webSite;
        this.size = builder.size;
        this.logoPath = builder.logoPath;
        this.establishment = builder.establishment;
        this.description = builder.description;
        this.createDate = builder.createDate;
        this.modifyDate = builder.modifyDate;
    }

    public static CompanyEntityBuilder toBuilder(Company company) {
        return new CompanyEntityBuilder()
                .withId(company.getId())
                .withName(company.getName())
                .withWebSite(company.getWebSite())
                .withSize(company.getSize())
                .withLogoPath(company.getLogoPath())
                .withEstablishment(company.getEstablishment())
                .withDescription(company.getDescription())
                .withCreateDate(company.getCreateDate())
                .withModifyDate(company.getModifyDate());
    }

    public static class CompanyEntityBuilder {

        private Long id;
        private String name;
        private String webSite;
        private String size;
        private String logoPath;
        private Integer establishment;
        private String description;
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

        public CompanyEntityBuilder withEstablishment(Integer establishment) {
            this.establishment = establishment;
            return this;
        }

        public CompanyEntityBuilder withDescription(String description) {
            this.description = description;
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

        public Company build() {
            return new Company(this);
        }
    }
}
