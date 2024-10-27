package com.example.taskmanagement.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for local testing, configure as needed for production
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/tasks/**").hasRole("USER")
                    .anyRequest().authenticated()
            )
            .httpBasic(withDefaults()) // Enable Basic Authentication
            .cors(withDefaults()); // Apply CORS configuration from WebMvcConfigurer

        return http.build();
    }
}
