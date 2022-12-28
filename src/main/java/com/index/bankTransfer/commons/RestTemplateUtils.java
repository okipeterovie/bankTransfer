package com.index.bankTransfer.commons;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateUtils {

    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofMillis(15000))
                      .setReadTimeout(Duration.ofMillis(15000))
                      .build();
    }

    public static HttpHeaders createPaystackHeaders(final String secretKey) {
        final String authHeader = "Bearer ".concat(secretKey);

        final LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.set("Authorization", authHeader);

        return new HttpHeaders(multiValueMap);
    }

    public static HttpHeaders createFlutterwaveHeaders(final String secretKey) {
        final String authHeader = "Bearer ".concat(secretKey);

        final LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.set("Authorization", authHeader);

        return new HttpHeaders(multiValueMap);
    }
}
