package edu.bbte.smartguide.springbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.google.maps.GeoApiContext;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SpringBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBackendApplication.class, args);
    }
}
