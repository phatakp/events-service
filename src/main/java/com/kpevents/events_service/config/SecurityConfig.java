package com.kpevents.events_service.config;

import com.kpevents.events_service.config.clerk.ClerkJwtAuthFilter;
import com.kpevents.events_service.entities.enums.UserRole;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AppProperties appProperties;
    private final ClerkJwtAuthFilter clerkJwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/members/pending").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/members/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/members/**").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/members/**").hasRole(UserRole.ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/transactions/**").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/transactions/**").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET,"/transactions/balance/**").permitAll()

                        .requestMatchers(HttpMethod.GET,"/transactions/donations/stats/**").permitAll()

                        .requestMatchers(HttpMethod.GET,"/transactions/expenses/**").permitAll()

                        .requestMatchers(HttpMethod.GET,"/transactions/annadaan/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/transactions/annadaan/bookings").permitAll()
                        .requestMatchers(HttpMethod.POST, "/transactions/annadaan").hasRole(UserRole.ADMIN.name())

                        .requestMatchers(HttpMethod.GET,"/transactions/temple/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/transactions/temple/bookings").permitAll()
                        .requestMatchers(HttpMethod.POST, "/transactions/temple").hasRole(UserRole.ADMIN.name())

                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(clerkJwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c -> {
                    c.authenticationEntryPoint(
                            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                    );
                    c.accessDeniedHandler((req,
                                           res,
                                           ex) -> res.setStatus(HttpStatus.FORBIDDEN.value()));
                });
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(configurationSource());
    }

    private UrlBasedCorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(appProperties.getAllowedOrigins()));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
