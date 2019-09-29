package getjobin.it.portal.elasticservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ElasticServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticServiceApplication.class, args);
	}

}
