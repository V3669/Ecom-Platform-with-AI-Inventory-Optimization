package com.ecom.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProphetModelService {
    private final WebClient webClient;
    private final String prophetServiceUrl;

    public ProphetModelService(WebClient.Builder webClientBuilder, 
                               @Value("${prophet.service.url:http://localhost:5000}") String prophetServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(prophetServiceUrl).build();
        this.prophetServiceUrl = prophetServiceUrl;
        log.info("Initialized ProphetModelService with service URL: {}", prophetServiceUrl);
    }

    public Map<LocalDate, Double> forecastDemand(List<Map<String, Object>> historicalData, int daysToForecast) {
        try {
            log.debug("Forecasting demand for {} days with {} historical data points", 
                    daysToForecast, historicalData.size());
            
            Map<String, Object> request = new HashMap<>();
            request.put("historical_data", historicalData);
            request.put("days_to_forecast", daysToForecast);
            
            return webClient.post()
                    .uri("/api/forecast")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<LocalDate, Double>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Error forecasting demand: ", e);
            throw new RuntimeException("Failed to forecast demand", e);
        }
    }

    public Map<LocalDate, Double> detectTrends(List<Map<String, Object>> historicalData) {
        try {
            log.debug("Detecting trends with {} historical data points", historicalData.size());
            
            Map<String, Object> request = new HashMap<>();
            request.put("historical_data", historicalData);
            
            return webClient.post()
                    .uri("/api/trends")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<LocalDate, Double>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Error detecting trends: ", e);
            throw new RuntimeException("Failed to detect trends", e);
        }
    }

    public Map<LocalDate, Double> detectSeasonality(List<Map<String, Object>> historicalData) {
        try {
            log.debug("Detecting seasonality with {} historical data points", historicalData.size());
            
            Map<String, Object> request = new HashMap<>();
            request.put("historical_data", historicalData);
            
            return webClient.post()
                    .uri("/api/seasonality")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<LocalDate, Double>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Error detecting seasonality: ", e);
            throw new RuntimeException("Failed to detect seasonality", e);
        }
    }
} 