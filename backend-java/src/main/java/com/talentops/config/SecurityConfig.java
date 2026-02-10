package com.talentops.config;

import com.talentops.authentication.utils.KeycloakRoleConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // without this, @PreAuthorize annotations won't work
@Slf4j
public class SecurityConfig {

    @Value("${keycloak.resource-client.client-id}")
    private String resourceClientId;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> {
                    csrf.disable();
//                    log.info("✅ CSRF disabled");
                })
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(
                            org.springframework.security.config.http.SessionCreationPolicy.STATELESS);
//                    log.info("✅ Session management set to STATELESS");
                })
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                            "/api/v1/auth/**",
                            "/health"
                    ).permitAll()
//                            .requestMatchers("/error").denyAll()
                            .anyRequest().authenticated();
//                    log.info("✅ Authorization rules configured");
                })
                .oauth2ResourceServer(oauth -> {
                    oauth.jwt(jwt -> {
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
//                        log.info("✅ JWT authentication configured");
                    });
                });
//        log.info("🎉 Security Filter Chain configured successfully");
        return  http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:4200","http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


   @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter(resourceClientId));
        return converter;
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}