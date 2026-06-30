package com.tfowl95.weather_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix="weather-api")
@Validated
public record WeatherApiProperties (
    @NotBlank String baseUrl,
    @NotBlank String apiKey,
    @Min(1) int cacheTtlSeconds,
    @Min(1) int rateLimit
) {}
