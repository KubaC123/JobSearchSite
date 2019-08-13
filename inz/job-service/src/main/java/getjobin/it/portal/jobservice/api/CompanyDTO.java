package getjobin.it.portal.jobservice.api;

import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class CompanyDTO {

    private Long id;
    private String name;
    private String webSite;
    private String size;
    private String logoPath;
}
