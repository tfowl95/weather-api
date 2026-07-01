package com.tfowl95.weather_api.exception;

public class RateLimitExceededException extends RuntimeException{

    public RateLimitExceededException(int rateLimit) {
        super("Rate limit exceeded - try again later. Maximum number of API calls per minute is: " + rateLimit);
    }

}
