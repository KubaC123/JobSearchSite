package getjobin.it.portal.jobservice.domain.indexation.boundary;

import getjobin.it.portal.elasticservice.api.MappingEventDto;
import org.springframework.stereotype.Component;

import javax.json.Json;

@Component
public class ElasticSearchMappingProvider {

    static final String TYPE = "type";

    MappingEventDto buildCompanyIndexMapping() {
        return MappingEventDto.builder()
                .indexName("company")
                .mapping(buildCompanyMapping())
                .build();
    }

    private String buildCompanyMapping() {
        return Json.createObjectBuilder()
                .add("properties", Json.createObjectBuilder()
                        .add("name", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("description", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("size", Json.createObjectBuilder()
                                .add(TYPE, FieldType.INTEGER.value())
                                .build())
                        .build())
                .build()
                .toString();
    }

    MappingEventDto buildJobIndexMapping() {
        return MappingEventDto.builder()
                .indexName("job")
                .mapping(buildJobMapping())
                .build();
    }

    private String buildJobMapping() {
        return Json.createObjectBuilder()
                .add("properties", Json.createObjectBuilder()
                        .add("type", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("title", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("companyName", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("categoryName", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("experienceLevel", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("employmentType", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("salaryMin", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("salaryMax", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("description", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("technologyName", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("projectDescription", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("active", Json.createObjectBuilder()
                                .add(TYPE, FieldType.BOOLEAN.value())
                                .build())
                        .add("techStacks", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("cities", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("countries", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .add("streets", Json.createObjectBuilder()
                                .add(TYPE, FieldType.TEXT.value())
                                .build())
                        .build())
                .build()
                .toString();
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
            return literal;
        }
    }
}
