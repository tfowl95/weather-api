package com.tfowl95.weather_api;
import java.time.Instant;

public record WeatherResponse (
    String cityCode,
    double temperatureC,
    double temperatureF,
    String description,
    String source,
    boolean cached,
    Instant fetchedAt
) {}