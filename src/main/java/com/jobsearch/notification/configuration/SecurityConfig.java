package com.jobsearch.notification.configuration;

import com.jobsearch.notification.security.converter.SupabaseJwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final SupabaseJwtConverter keycloakJwtConverter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
                                   .requestMatchers("/v3/api-docs/**",
                                                    "/swagger-ui/**",
                                                    "/swagger-ui.html",
                                                    "/api/v1/external-scheduler/**").permitAll()
                                   .anyRequest().authenticated()
                              )
        .oauth2ResourceServer(oauth2 -> oauth2
                                  .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtConverter))
                             );
    return http.build();
  }
}
