package com.tfowl95.weather_api;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @GetMapping("/{cityCode}")
    public WeatherResponse getWeather(@PathVariable String cityCode) {
        return new WeatherResponse(
                cityCode,
                20.0,
                68.0,
                "Clear skies",
                "hardcoded",
                false,
                Instant.now()
        );
    }
}
