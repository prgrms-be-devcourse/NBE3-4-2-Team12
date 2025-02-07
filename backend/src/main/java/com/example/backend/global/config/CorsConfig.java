package com.example.backend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CorsConfig
 * Cors설정 클래스
 * @author 100minha
 */
@Configuration
public class CorsConfig {

	private final String clientBaseUrl;

	public CorsConfig(@Value("${CLIENT_BASE_URL}") String clientBaseUrl) {
		this.clientBaseUrl = clientBaseUrl;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowCredentials(true);
		corsConfig.addAllowedOrigin(clientBaseUrl);
		corsConfig.addAllowedHeader("*");
		corsConfig.addExposedHeader("Authorization");
		corsConfig.addAllowedMethod("*");

		source.registerCorsConfiguration("/**", corsConfig);
		return new CorsFilter(source);
	}
}
