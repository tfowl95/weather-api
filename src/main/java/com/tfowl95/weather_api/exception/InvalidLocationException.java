package com.tfowl95.weather_api.exception;

public class InvalidLocationException extends RuntimeException{

    public InvalidLocationException(Exception ex) {
        super("Invalid location or the provider rejected the response", ex);
    }

}
