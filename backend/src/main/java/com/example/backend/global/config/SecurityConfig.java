package com.example.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.backend.global.auth.jwt.AdminAuthFilter;
import com.example.backend.global.auth.jwt.MemberAuthFilter;

import lombok.RequiredArgsConstructor;

/**
 * SecurityConfig
 * 시큐리티 관련 설정 클래스
 * @author 100minha
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final MemberAuthFilter memberAuthFilter;
	private final AdminAuthFilter adminAuthFilter;
	private final CorsConfig corsConfig;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf
						.ignoringRequestMatchers("/h2-console/**")  // H2 콘솔 사용을 위해 CSRF 비활성화
						.disable()
				)
				.headers(headers -> headers
						.frameOptions(
								frameOptions -> frameOptions.disable()  // H2 콘솔 화면을 위해 필요
						)
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.httpBasic(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.logout(AbstractHttpConfigurer::disable)
				.addFilter(corsConfig.corsFilter())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/h2-console/**").permitAll()  // H2 콘솔 접근 허용
						.anyRequest().permitAll()
				)
				.addFilterBefore(memberAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(adminAuthFilter, UsernamePasswordAuthenticationFilter.class)
		;

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws
		Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}