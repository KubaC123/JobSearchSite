package getjobin.it.portal.jobservice.client;

import getjobin.it.portal.elasticservice.api.SearchRequestDto;
import getjobin.it.portal.elasticservice.api.SearchResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "elastic-service", url="http://localhost:9020/")
public interface ElasticServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "search/fullText", produces = "application/json")
    SearchResultDto fullTextSearch(@RequestBody SearchRequestDto searchRequest);
}
