package getjobin.it.portal.jobservice.client;

import getjobin.it.portal.elasticservice.api.SearchResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("elastic-service")
public interface ElasticServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "search/fullText")
    SearchResultDto fullTextSearch(@RequestParam("indexName") String indexName,
                                   @RequestParam("searchText") String searchText,
                                   @RequestParam("fields") String commaSeparatedFields);

}
