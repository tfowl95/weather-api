# Weather API

A lightweight Spring Boot REST API that fetches weather data for a given location, featuring Redis-backed caching and per-client rate limiting.

---

## Overview

This service exposes an endpoint to retrieve weather data. It queries a third-party weather provider using Spring's `RestClient`, caches responses in Redis to minimize upstream API calls, and enforces rate limiting based on the client's IP address.

---

## Concepts Demonstrated

* RESTful API development with Spring Boot
* External API consumption using Spring's `RestClient`
* Caching and Rate Limiting using Redis (`StringRedisTemplate`)
* Type-safe configuration mapping via `@ConfigurationProperties`
* Global exception handling with `@RestControllerAdvice`

---

## Features

* **Weather Lookup:** Fetches real-time weather data by location.
* **Redis Caching:** Configurable Time-To-Live (TTL) for cached weather responses to reduce latency and API costs.
* **Rate Limiting:** IP-based request throttling using Redis to prevent abuse.
* **Graceful Error Handling:** Custom exceptions mapped to standard HTTP statuses (400, 429, 502).

---

## Project Structure

```
src/main/java/com/tfowl95/weather_api/
├── WeatherApiApplication.java       # Application entry point
├── client/                          # RestClient configuration
├── config/                          # Configuration properties mapping
├── controller/                      # REST endpoint controllers
├── exception/                       # Custom exceptions and GlobalExceptionHandler
└── service/                         # Business logic, caching, and API interactions
```

---

## Requirements

* Java 17 or higher
* Spring Boot
* Redis server (running locally or remotely)
* Third-party Weather API Key

---

## Configuration

Add the following properties to your application.yml or application.properties:

```
weather-api.base-url=https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/
weather-api.api-key=YOUR_API_KEY
weather-api.cache-ttl-seconds=600
weather-api.rate-limit=10
```

---

## Usage

Start the Spring Boot application and make a GET request to the weather endpoint:

```
curl http://localhost:8080/weather-api/<location>
```

Example:

```
curl http://localhost:8080/weather-api/London
```

---

## How It Works

1. A client makes a GET request to /weather-api/{location}.
2. The WeatherService extracts the client's IP address and checks the current minute's request count in Redis.
3. If the rate limit is exceeded, a 429 Too Many Requests error is immediately returned.
4. If within limits, the service checks Redis for a cached response for the requested location.
5. If a cache miss occurs, a request is made to the upstream weather provider using RestClient.
6. The upstream response is saved in the Redis cache with the configured TTL.
7. The JSON response is returned to the client.

---

## Error Handling

The application uses a @RestControllerAdvice to handle exceptions globally and return appropriate HTTP status codes:

* 400 Bad Request: Triggered when the upstream API rejects the location (`InvalidLocationException`).
* 429 Too Many Requests: Triggered when the client exceeds the configured RPM (`RateLimitExceededException`).
* 502 Bad Gateway: Triggered when the upstream provider returns an error or is unreachable (`UpstreamWeatherException`).

---

## License

No license specified.

---