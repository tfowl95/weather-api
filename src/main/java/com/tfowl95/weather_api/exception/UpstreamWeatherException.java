package com.tfowl95.weather_api.exception;

public class UpstreamWeatherException extends RuntimeException{

    public UpstreamWeatherException(String message, Exception ex) {
        super(message, ex);
    }

}
