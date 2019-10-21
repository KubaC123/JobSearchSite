package getjobin.it.portal.elasticservice.domain.boundary;

import getjobin.it.portal.elasticservice.api.SearchResultDto;
import getjobin.it.portal.elasticservice.client.control.ESJavaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = SearchResource.SEARCH_PATH)
public class SearchResource {

    static final String SEARCH_PATH = "search";

    private ESJavaClient esJavaClient;

    @Autowired
    public SearchResource(ESJavaClient esJavaClient) {
        this.esJavaClient = esJavaClient;
    }

    @RequestMapping(method = RequestMethod.GET, value = "fullText")
    public SearchResultDto fullTextSearch(@RequestParam("index") String indexName,
                                          @RequestParam("searchText") String searchText,
                                          @RequestParam("fields") String commaSeparatedFields) {
        return esJavaClient.fullTextSearch(indexName, searchText, getFieldsAsList(commaSeparatedFields));
    }

    private List<String> getFieldsAsList(String commaSeparatedFields) {
        return Arrays.stream(commaSeparatedFields.split(","))
                .collect(Collectors.toList());
    }
}
