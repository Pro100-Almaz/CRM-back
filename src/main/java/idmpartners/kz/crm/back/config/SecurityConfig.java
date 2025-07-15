package idmpartners.kz.crm.back.config;

import idmpartners.kz.crm.back.converter.KeyCloakConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

//checking
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(keyCloakConverter())))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/user/create").permitAll()
                        .requestMatchers("/login/**").permitAll()

                        .anyRequest().authenticated()
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();


    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public KeyCloakConverter keyCloakConverter() {
        return new KeyCloakConverter();
    }
}
