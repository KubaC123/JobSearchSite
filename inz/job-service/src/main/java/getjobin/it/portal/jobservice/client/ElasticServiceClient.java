package getjobin.it.portal.jobservice.client;

import getjobin.it.portal.elasticservice.api.SearchResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "elastic-service", url="http://localhost:9020/")
public interface ElasticServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "search/fullText", produces = "application/json")
    SearchResultDto fullTextSearch(@RequestParam("index") String indexName,
                                   @RequestParam("searchText") String searchText,
                                   @RequestParam("fields") String commaSeparatedFields);

}
