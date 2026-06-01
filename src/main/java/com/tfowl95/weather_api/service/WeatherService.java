package com.tfowl95.weather_api.service;

import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    public String fetchWeather(String location) {

        return "Test response from service" + location;
        // check redis and return if it is there

        // grab third party info and store it in redis
    }
}
