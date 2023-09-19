package it.unisalento.pas.taxbe.configurations;

import it.unisalento.pas.taxbe.security.JwtTokenConverter;
import it.unisalento.pas.taxbe.security.JwtTokenValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuration class responsible for setting up security features in the backend application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Create an instance of JwtTokenConverter
    private final JwtTokenConverter jwtTokenConverter = new JwtTokenConverter();

    /**
     * Configure the security filter chain for HTTP requests.
     *
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception If there's an error during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure Cross-Origin Resource Sharing (CORS)
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        // Disable Cross-Site Request Forgery (CSRF) protection
        http.csrf(AbstractHttpConfigurer::disable);

        // Configure authorization rules for different HTTP endpoints
        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/api/fee/create/all").hasAnyAuthority(SecurityConstants.ADMIN_ROLE_ID)
                        .requestMatchers(HttpMethod.DELETE, "/api/fee/delete/{feeId}").hasAnyAuthority(SecurityConstants.ADMIN_ROLE_ID)
                        .requestMatchers(HttpMethod.POST, "/api/fee/pay/{feeId}").hasAnyAuthority(SecurityConstants.USER_ROLE_ID)
                        .requestMatchers(HttpMethod.GET, "/api/fee/get/user/{userId}").hasAnyAuthority(SecurityConstants.USER_ROLE_ID)
                        .requestMatchers(HttpMethod.GET, "/api/fee/get/all").hasAnyAuthority(SecurityConstants.ADMIN_ROLE_ID)

                        .requestMatchers(HttpMethod.GET, "/api/stats/all/{year}/{paidStatus}").hasAnyAuthority(SecurityConstants.ADMIN_ROLE_ID)
                        .requestMatchers(HttpMethod.GET, "/api/stats/user/{userId}/{year}/{paidStatus}").hasAnyAuthority(SecurityConstants.USER_ROLE_ID)
                        .anyRequest().denyAll())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtTokenConverter)
                ))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    /**
     * Configure the CORS (Cross-Origin Resource Sharing) settings.
     *
     * @return The CorsConfigurationSource with CORS settings.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configure the JWT (JSON Web Token) decoder for authentication.
     *
     * @return The configured JwtDecoder.
     */
    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(SecurityConstants.ISSUER_LIST[0]);
        OAuth2TokenValidator<Jwt> tokenValidator = new JwtTokenValidator();
        jwtDecoder.setJwtValidator(tokenValidator);
        return jwtDecoder;
    }
}
