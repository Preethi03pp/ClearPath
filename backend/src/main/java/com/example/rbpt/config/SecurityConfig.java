package com.example.rbpt.config;

import com.example.rbpt.repo.UserRepository;
import com.example.rbpt.security.JwtAuthenticationFilter;
import com.example.rbpt.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  @Value("${app.jwt.secret}") 
  private String jwtSecret;

  @Value("${app.cors.allowedOrigins}") 
  private String allowedOrigins;

  @Bean
  public JwtUtil jwtUtil() {
    return new JwtUtil(jwtSecret, 1000L * 60 * 60 * 12);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, UserRepository userRepository) throws Exception {
    http.csrf(csrf -> csrf.disable());
    http.cors(Customizer.withDefaults());  // Enables CORS support in Spring Security
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/files/**").authenticated()
        .anyRequest().authenticated()
    );
    http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil(), userRepository),
        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  // Global CORS configuration to allow your frontend origins
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins.split(","))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
      }
    };
  }

  @Bean 
  public PasswordEncoder passwordEncoder() { 
    return new BCryptPasswordEncoder(); 
  }

  @Bean 
  public AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {
    return c.getAuthenticationManager();
  }
}
