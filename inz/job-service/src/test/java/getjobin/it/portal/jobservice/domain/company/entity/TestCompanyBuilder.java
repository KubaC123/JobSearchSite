package getjobin.it.portal.jobservice.domain.company.entity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestCompanyBuilder {

    public static final String TEST_COMPANY_NAME = "Cichy kącik";
    public static final String TEST_COMPANY_WEBSITE = "www.cichykacik.pl";
    public static final String TEST_COMPANY_SIZE = "1";
    private static final String UPDATE = "update";

    public static Company buildValidCompany() {
        return Company.builder()
                .withName(TEST_COMPANY_NAME)
                .withWebSiteUrl(TEST_COMPANY_WEBSITE)
                .withSize(TEST_COMPANY_SIZE)
                .build();
    }

    public static List<Company> buildValidCompanies(int numberOfCompanies) {
        return IntStream.rangeClosed(1, numberOfCompanies)
                .mapToObj(index -> Company.builder()
                        .withName(TEST_COMPANY_NAME + index)
                        .withWebSiteUrl(TEST_COMPANY_WEBSITE)
                        .withSize(TEST_COMPANY_SIZE)
                        .build())
                .collect(Collectors.toList());
    }

    public static Company buildCompanyWithNullName() {
        return Company.builder()
                .withWebSiteUrl(TEST_COMPANY_WEBSITE)
                .withSize(TEST_COMPANY_SIZE)
                .build();
    }

    public static Company buildCompanyWithEmptyName() {
        return Company.builder()
                .withName("")
                .withWebSiteUrl(TEST_COMPANY_WEBSITE)
                .withSize(TEST_COMPANY_SIZE)
                .build();
    }

    public static Company buildValidUpdatedCompany(Company createdCompany) {
        return Company.toBuilder(createdCompany)
                .withName(TEST_COMPANY_NAME + UPDATE)
                .withWebSiteUrl(TEST_COMPANY_WEBSITE + UPDATE)
                .build();
    }

}
