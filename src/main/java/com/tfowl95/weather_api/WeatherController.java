package com.tfowl95.weather_api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.Instant;


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
