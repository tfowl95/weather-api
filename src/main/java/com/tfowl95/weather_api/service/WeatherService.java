package com.tfowl95.weather_api.service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import com.tfowl95.weather_api.config.WeatherApiProperties;
import com.tfowl95.weather_api.exception.InvalidLocationException;
import com.tfowl95.weather_api.exception.RateLimitExceededException;
import com.tfowl95.weather_api.exception.UpstreamWeatherException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class WeatherService {
    private final RestClient restClient;
    private final WeatherApiProperties properties;
    private final StringRedisTemplate redis;
    private final HttpServletRequest request;
    private static final DateTimeFormatter BUCKET_FORMAT =
        DateTimeFormatter.ofPattern("yyyyMMddHHmm").withZone(ZoneOffset.UTC);

    public WeatherService(RestClient restClient, WeatherApiProperties properties, StringRedisTemplate redis, HttpServletRequest request) {
        this.restClient = restClient;
        this.properties = properties;
        this.redis = redis;
        this.request = request;
    }

    public String fetchWeather(String location) {
        
        // Rate limiting

        String clientAddress = request.getRemoteAddr();
        String rateLimitKey = "weather:rl" + clientAddress + BUCKET_FORMAT.format(Instant.now());
        Long requestCount = redis.opsForValue().increment(rateLimitKey);

        if (requestCount != null && requestCount == 1L) {
            redis.expire(rateLimitKey, Duration.ofMinutes(properties.rateLimit()));
        }

        if (requestCount != null && requestCount > properties.rateLimit()) {
            throw new RateLimitExceededException(properties.rateLimit());
        }


        // check redis cache for store value

        String cacheKey = "weather:cache:" + location;
        String cachedResponse = redis.opsForValue().get(cacheKey);
        if (cachedResponse != null) return cachedResponse;


        // Pull from 3rd party provider if not in cache

        try {

            String response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                    .pathSegment(location)
                    .queryParam("key", properties.apiKey())
                    .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
            
            redis.opsForValue().set(cacheKey, response, Duration.ofSeconds(properties.cacheTtlSeconds()));

            return response;

        } catch(RestClientResponseException ex) {
            if (ex.getStatusCode().is4xxClientError()) throw new InvalidLocationException(ex);
            else throw new UpstreamWeatherException("Invalid location or provider rejected request.", ex);
        } catch(RestClientException ex) {
            throw new UpstreamWeatherException("Provider returned an error.", ex);
        }
    }
}
