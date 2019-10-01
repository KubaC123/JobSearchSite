package getjobin.it.portal.jobservice.domain.search;

import org.springframework.stereotype.Component;

@Component
public class ElasticSearchMappingProvider {

    public String getCompanyMapping() {
        return "{\n"
                + "\"properties\":"
                + "{\n"
                + "\"name\":" + JSONMappingType.TEXT.getValue() + ","
                + "\"description\":" + JSONMappingType.TEXT.getValue() + ","
                + "\"size\":" + JSONMappingType.INTEGER.getValue()
                + "}\n"
                + "}\n";
    }

    private enum JSONMappingType {
        TEXT("text"),
        KEYWORD("keyword"),
        INTEGER("integer"),
        DATE("date");

        private String dataType;

        JSONMappingType(String dataType) {
            this.dataType = dataType;
        }

        public String getValue() {
            return "{ \"type\": \"" + dataType + "\" } \n";
        }
    }
}
