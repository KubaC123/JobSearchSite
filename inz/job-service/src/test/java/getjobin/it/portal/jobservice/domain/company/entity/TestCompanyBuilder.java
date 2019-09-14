package getjobin.it.portal.jobservice.domain.company.entity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestCompanyBuilder {

    public static final String NAME = "Cichy kącik";
    public static final String WEBSITE = "www.cichykacik.pl";
    public static final String SIZE = "1";
    public static final String UPDATE = "update";

    public static Company buildValidCompany() {
        return Company.builder()
                .withName(NAME)
                .withWebSiteUrl(WEBSITE)
                .withSize(SIZE)
                .build();
    }

    public static List<Company> buildValidCompanies(int numberOfCompanies) {
        return IntStream.rangeClosed(1, numberOfCompanies)
                .mapToObj(index -> Company.builder()
                        .withName(NAME + index)
                        .withWebSiteUrl(WEBSITE)
                        .withSize(SIZE)
                        .build())
                .collect(Collectors.toList());
    }

    public static Company buildCompanyWithNullName() {
        return Company.builder()
                .withWebSiteUrl(WEBSITE)
                .withSize(SIZE)
                .build();
    }

    public static Company buildCompanyWithEmptyName() {
        return Company.builder()
                .withName("")
                .withWebSiteUrl(WEBSITE)
                .withSize(SIZE)
                .build();
    }

    public static Company buildValidUpdatedCompany(Company existingCompany) {
        return Company.toBuilder(existingCompany)
                .withName(NAME + UPDATE)
                .withWebSiteUrl(WEBSITE + UPDATE)
                .build();
    }

}
