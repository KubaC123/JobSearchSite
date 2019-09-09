package getjobin.it.portal.jobservice.api;

import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class CompanyDTO {

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
}
