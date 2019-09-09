package getjobin.it.portal.jobservice.domain.company.entity;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import getjobin.it.portal.jobservice.domain.joboffer.entity.JobOffer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

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

    @Column(name = "WEBSITE_URL")
    private String webSiteUrl;

    @Column(name = "SIZE")
    private String size;

    @Column(name = "LOGO_URL")
    private String logoUrl;

    @Column(name = "ESTABLISHMENT")
    private Integer establishment;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "INSTAGRAM_URL")
    private String instagramUrl;

    @Column(name = "FACEBOOK_URL")
    private String facebookUrl;

    @Column(name = "LINKEDIN_URL")
    private String linkedinUrl;

    @Column(name = "TWITTER_URL")
    private String twitterUrl;

    @Setter
    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Setter
    @Column(name = "MODIFY_DATE")
    private Date modifyDate;

    @OneToMany(mappedBy = "company")
    private List<JobOffer> jobOffers;

    public static CompanyEntityBuilder builder() {
        return new CompanyEntityBuilder();
    }

    public Company() { }

    private Company(CompanyEntityBuilder builder) {
        this.id = builder.id;
        this.name= builder.name;
        this.webSiteUrl = builder.webSiteUrl;
        this.size = builder.size;
        this.logoUrl = builder.logoUrl;
        this.establishment = builder.establishment;
        this.description = builder.description;
        this.instagramUrl = builder.instagramUrl;
        this.facebookUrl = builder.facebookUrl;
        this.linkedinUrl = builder.linkedinUrl;
        this.twitterUrl = builder.twitterUrl;
        this.createDate = builder.createDate;
        this.modifyDate = builder.modifyDate;
    }

    public static CompanyEntityBuilder toBuilder(Company company) {
        return new CompanyEntityBuilder()
                .withId(company.getId())
                .withName(company.getName())
                .withWebSiteUrl(company.getWebSiteUrl())
                .withSize(company.getSize())
                .withLogoUrl(company.getLogoUrl())
                .withEstablishment(company.getEstablishment())
                .withDescription(company.getDescription())
                .withInstagramUrl(company.getInstagramUrl())
                .withFacebookUrl(company.getFacebookUrl())
                .withLinkedinUrl(company.getLinkedinUrl())
                .withTwitterUrl(company.getTwitterUrl())
                .withCreateDate(company.getCreateDate())
                .withModifyDate(company.getModifyDate());
    }

    public static class CompanyEntityBuilder {

        private Long id;
        private String name;
        private String webSiteUrl;
        private String size;
        private String logoUrl;
        private Integer establishment;
        private String description;
        private String instagramUrl;
        private String facebookUrl;
        private String linkedinUrl;
        private String twitterUrl;
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

        public CompanyEntityBuilder withWebSiteUrl(String webSiteUrl) {
            this.webSiteUrl = webSiteUrl;
            return this;
        }

        public CompanyEntityBuilder withSize(String size) {
            this.size = size;
            return this;
        }

        public CompanyEntityBuilder withLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
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

        public CompanyEntityBuilder withInstagramUrl(String instagramUrl) {
            this.instagramUrl = instagramUrl;
            return this;
        }

        public CompanyEntityBuilder withFacebookUrl(String facebookUrl) {
            this.facebookUrl = facebookUrl;
            return this;
        }

        public CompanyEntityBuilder withLinkedinUrl(String linkedinUrl) {
            this.linkedinUrl = linkedinUrl;
            return this;
        }

        public CompanyEntityBuilder withTwitterUrl(String twitterUrl) {
            this.twitterUrl = twitterUrl;
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
