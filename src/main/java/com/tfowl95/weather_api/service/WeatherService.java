package com.tfowl95.weather_api.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.tfowl95.weather_api.config.WeatherApiProperties;

@Service
public class WeatherService {
    private final RestClient restClient;
    private final WeatherApiProperties properties;

    public WeatherService(RestClient restClient, WeatherApiProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public String fetchWeather(String location) {
        

        // check redis and return if it is there

        // grab third party info and store it in redis
        String response = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment(location)
                .queryParam("key", properties.apiKey())
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(String.class);
        
        return response;
    }
}
