package getjobin.it.portal.jobservice.domain.search;

import org.springframework.stereotype.Component;

@Component
public class ElasticSearchMappingProvider {

    public String buildCompanyMapping() {
        return
            "{\n"
            + "\"properties\":"
                + "{\n"
                    + "\"name\":" + "{ " + FieldType.TEXT.value() + "},\n"
                    + "\"description\":" + "{ " + FieldType.TEXT.value() + "},\n"
                    + "\"size\":" + "{ " + FieldType.INTEGER.value() + "}\n"
                + "}\n"
            + "}\n";
    }

    public String buildJobMapping() {
        return
            "{\n"
            + "\"properties\":"
                + "{\n"
                    + "\"type\":" + "{ " + FieldType.KEYWORD.value() + "},\n"
                    + "\"title\":" + "{ " + FieldType.TEXT.value() + "},\n"
                    + "\"companyId\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"companyName\":" + "{ " + FieldType.TEXT.value() + "},\n"
                    + "\"categoryId\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"categoryName\":" + "{ " + FieldType.TEXT.value() + "},\n"
                    + "\"experienceLevel\":" + "{ " + FieldType.KEYWORD.value() + "},\n"
                    + "\"employmentType\":" + "{ " + FieldType.KEYWORD.value() + "},\n"
                    + "\"salaryMin\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"salaryMax\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"flexibleWorkHours\":" + "{ " + FieldType.BOOLEAN.value() + "},\n"
                    + "\"description\":" + "{ " + FieldType.TEXT.value() + "},\n"
                    + "\"technologyId\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"technologyName\":" + "{ " + FieldType.TEXT.value() + "},\n"
                    + "\"projectDescription\":" + "{ " + FieldType.TEXT.value() + "},\n"
                    + "\"development\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"testing\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"maintenance\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"clientSupport\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"meetings\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"leading\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"documentation\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"otherActivities\":" + "{ " + FieldType.INTEGER.value() + "},\n"
                    + "\"active\":" + "{ " + FieldType.BOOLEAN.value() + "},\n"
                    + "\"techStacks\":" + "{ " + FieldType.NESTED.value() + ",\n"
                        + "\"properties\":"
                            + "{\n"
                                + "\"techStackId\":" + "{ " +  FieldType.INTEGER.value() + "},\n"
                                + "\"techStackName\":" + "{ " +  FieldType.TEXT.value() + "},\n"
                                + "\"techStackExperienceLevel\":" + "{ " +  FieldType.INTEGER.value() + "}\n"
                            + "}\n"
                        + "}\n"
                + "}\n"
            + "}\n";
    }


    private enum FieldType {
        TEXT("text"),
        KEYWORD("keyword"),
        INTEGER("integer"),
        BOOLEAN("boolean"),
        NESTED("nested"),
        DATE("date");

        private String literal;

        FieldType(String literal) {
            this.literal = literal;
        }

        public String value() {
            return "\"type\": \"" + literal + "\"";
        }
    }
}
