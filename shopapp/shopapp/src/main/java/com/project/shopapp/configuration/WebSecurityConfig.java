package com.project.shopapp.configuration;

import com.project.shopapp.filter.JwtFilter;
import com.project.shopapp.model.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtFilter jwtFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(
                                        String.format("%s/users/register", apiPrefix),
                                        String.format("%s/users/login", apiPrefix)
                                )
                                .permitAll()
                                .requestMatchers(
                                        String.format("%s/products**", apiPrefix)).permitAll()

                                .requestMatchers(
                                        String.format("%s/products/**", apiPrefix)).permitAll()

                                .requestMatchers(GET,
                                        String.format("%s/roles**", apiPrefix)).permitAll()

                                .requestMatchers(GET,
                                        String.format("%s/categories**", apiPrefix)).permitAll()

                                .requestMatchers(GET,
                                        String.format("%s/categories/**", apiPrefix)).permitAll()

                                .requestMatchers(POST,
                                        String.format("%s/categories/**", apiPrefix)).hasAnyRole(RoleType.ADMINISTRATOR)

                                .requestMatchers(PUT,
                                        String.format("%s/categories/**", apiPrefix)).hasAnyRole(RoleType.ADMINISTRATOR)

                                .requestMatchers(DELETE,
                                        String.format("%s/categories/**", apiPrefix)).hasAnyRole(RoleType.ADMINISTRATOR)

                                .requestMatchers(GET,
                                        String.format("%s/products**", apiPrefix)).permitAll()

                                .requestMatchers(GET,
                                        String.format("%s/products/**", apiPrefix)).permitAll()

                                .requestMatchers(GET,
                                        String.format("%s/products/images/*", apiPrefix)).permitAll()
                                .requestMatchers(POST,
                                        String.format("%s/products/uploads**", apiPrefix)).permitAll()

                                .requestMatchers(POST,
                                        String.format("%s/products/uploads/**", apiPrefix)).permitAll()

                                .requestMatchers(POST,
                                        String.format("%s/products**", apiPrefix)).hasAnyRole(RoleType.ADMINISTRATOR)

                                .requestMatchers(PUT,
                                        String.format("%s/products/**", apiPrefix)).hasAnyRole(RoleType.ADMINISTRATOR)

                                .requestMatchers(DELETE,
                                        String.format("%s/products/**", apiPrefix)).hasAnyRole(RoleType.ADMINISTRATOR)


                                .requestMatchers(POST,
                                        String.format("%s/orders/**", apiPrefix)).hasAnyRole(RoleType.CUSTOMER)

                                .requestMatchers(GET,
                                        String.format("%s/orders/**", apiPrefix)).permitAll()

                                .requestMatchers(PUT,
                                        String.format("%s/orders/**", apiPrefix)).hasRole(RoleType.ADMINISTRATOR)

                                .requestMatchers(DELETE,
                                        String.format("%s/orders/**", apiPrefix)).hasRole(RoleType.ADMINISTRATOR)

                                .requestMatchers(POST,
                                        String.format("%s/order_details/**", apiPrefix)).hasAnyRole(RoleType.CUSTOMER)

                                .requestMatchers(GET,
                                        String.format("%s/order_details/**", apiPrefix)).permitAll()

                                .requestMatchers(PUT,
                                        String.format("%s/order_details/**", apiPrefix)).hasRole(RoleType.ADMINISTRATOR)

                                .requestMatchers(DELETE,
                                        String.format("%s/order_details/**", apiPrefix)).hasRole(RoleType.ADMINISTRATOR)

                                .anyRequest().authenticated());
       httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));
        return httpSecurity.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token") );
        configuration.setExposedHeaders(List.of("x-auth-token"));
//        configuration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
