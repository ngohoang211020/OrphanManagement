package com.orphan.config;

/**
 *  The class for rest template configuration.
 *
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean(name = "restTemplate")
    public RestTemplate aiRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000); // 1 minute
        factory.setReadTimeout(60000); // 3 minutes

        RestTemplate restTemplate = new RestTemplate(factory);

        return restTemplate;
    }
}
