package com.example.backend.global.config;


import com.example.backend.global.auth.jwt.AdminAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import com.example.backend.global.auth.jwt.MemberAuthFilter;


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
	public SecurityFilterChain memberFilterChain(HttpSecurity http) throws Exception {
		http
			.securityMatchers(sm -> sm
				.requestMatchers("/members/"))
			.addFilterBefore(memberAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.addFilter(corsConfig.corsFilter())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/groups").permitAll()
				.anyRequest().permitAll()    //TODO: 백엔드 로직 작성 단계에서 테스트용 api별 권한 설정이므로 추후 올바르게 설정 필요
			)
			.addFilterBefore(adminAuthFilter, UsernamePasswordAuthenticationFilter.class)     // JWT 필터 추가
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



