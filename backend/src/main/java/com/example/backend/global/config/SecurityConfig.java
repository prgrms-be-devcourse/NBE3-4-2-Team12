package com.example.backend.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        (auth) -> auth.requestMatchers("/**").permitAll() // "/"경로를 모든 사용자에게 허용.
                                .requestMatchers("/admin/**").hasRole("ADMIN") // '/admin' 경로는 ADMIN 권한이 있어야 접근 가능.
                                .requestMatchers("/user/**").authenticated()  // '/user' 경로는 인증된 사용자만 접근 가능.
                );

        return http.build();
    }
}
