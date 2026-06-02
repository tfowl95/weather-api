package com.tfowl95.weather_api.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.tfowl95.weather_api.config.WeatherApiProperties;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient.Builder builder() {
        return RestClient.builder();
    }

    @Bean
    RestClient weatherRestClient(RestClient.Builder builder, WeatherApiProperties properties) {
        return builder
            .baseUrl(properties.baseUrl())
            .build();
    }

}
