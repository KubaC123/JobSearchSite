package getjobin.it.portal.elasticservice.client.boundary;

import getjobin.it.portal.elasticservice.api.SearchRequestDto;
import getjobin.it.portal.elasticservice.api.SearchResultDto;
import getjobin.it.portal.elasticservice.client.control.ESJavaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = SearchResource.SEARCH_PATH)
public class SearchResource {

    static final String SEARCH_PATH = "search";

    private ESJavaClient esJavaClient;

    @Autowired
    public SearchResource(ESJavaClient esJavaClient) {
        this.esJavaClient = esJavaClient;
    }

    @RequestMapping(method = RequestMethod.POST, value = "fullText")
    public SearchResultDto fullTextSearch(@RequestBody SearchRequestDto searchRequest) {
        return esJavaClient.fullTextSearch(searchRequest);
    }
}
