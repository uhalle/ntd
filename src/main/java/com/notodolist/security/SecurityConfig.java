package com.notodolist.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .headers(headers -> headers
	            .frameOptions(frame -> frame.disable())
	        )
	        .authorizeHttpRequests(auth -> auth
	            .anyRequest().permitAll()
	        );

	    return http.build();
	}
	
	
   
//	 @Bean
//	    public SecurityFilterChain securityFilterChain(HttpSecurity http)
//	            throws Exception {
//
//	        http
//	            .cors(Customizer.withDefaults())
//
//	            .csrf(csrf -> csrf.disable())
//	            .headers(headers -> headers
//	                    .frameOptions(frame -> frame.sameOrigin())
//	                )
//
//	            .authorizeHttpRequests(auth -> auth
//	                // Allow CORS preflight requests
//	                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//
//	                // Allow signup without authentication
//	                .requestMatchers("/api/signup").permitAll()
//	                .requestMatchers("/api/login").permitAll()
//	                .requestMatchers("/tasks").permitAll()
//
//	                // Allow H2 console
//	                .requestMatchers("/h2-console/**","/h2-console").permitAll()
//
//	                // Everything else requires authentication
//	                .anyRequest().authenticated()
//	            )
//
//	            .formLogin(Customizer.withDefaults());
//
//	        return http.build();
//	    }
//    
//   
//	@Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOrigins(
//            List.of("http://localhost:5173")
//        );
//
//        configuration.setAllowedMethods(
//            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
//        );
//
//        configuration.setAllowedHeaders(
//            List.of("*")
//        );
//
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source =
//            new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }

}
	