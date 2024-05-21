package edu.bbte.smartguide.springbackend.configurations;

import com.google.maps.GeoApiContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GoogleMapsConfig {

    @Value("AIzaSyCvBAXmYfvu3ptqfR7C75jC5YI6jR961s8")
    private String apiKey;

    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }
}