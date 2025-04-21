package com.bookmyturf.BookMyTurf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig  implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("call add cors mappings");
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")  // Allow Angular frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Add allowed methods
                .allowedHeaders("*")  // Allow any headers
                .allowCredentials(true);  // If using credentials (cookies, etc.)
    }
}
