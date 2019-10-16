package getjobin.it.portal.jobservice.infrastructure.config.swagger;

import com.google.common.collect.ImmutableList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String PATH_SELECTOR_PATTERN = "/api/*";
    private static final int OK_STATUS_CODE = 200;
    private static final int PRECONDITION_FAILED_STATUS_CODE = 412;
    private static final int INTERNAL_SERVER_ERROR_STATUS_CODE = 500;

    private static final List<ResponseMessage> responseMessages = ImmutableList.of(
            getStatusCode200Message(),
            getStatusCode412Message(),
            getStatusCode500Message());

    private static ResponseMessage getStatusCode200Message() {
        return new ResponseMessageBuilder()
                .code(OK_STATUS_CODE)
                .message("Request successful")
                .build();
    }

    private static ResponseMessage getStatusCode412Message() {
        return new ResponseMessageBuilder()
                .code(PRECONDITION_FAILED_STATUS_CODE)
                .message("Invalid data")
                .build();
    }

    private static ResponseMessage getStatusCode500Message() {
        return new ResponseMessageBuilder()
                .code(OK_STATUS_CODE)
                .message("Server responded with error")
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant(PATH_SELECTOR_PATTERN))
                .build();
    }


}
