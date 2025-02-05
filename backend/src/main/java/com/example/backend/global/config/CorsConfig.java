package com.example.backend.global.config;

import java.util.List;

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

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedOrigins(List.of("http://localhost:3000", "https://localhost:3000"));
		corsConfig.addAllowedHeader("*");
		corsConfig.addExposedHeader("Authorization");
		corsConfig.addAllowedMethod("*");

		source.registerCorsConfiguration("/**", corsConfig);
		return new CorsFilter(source);
	}
}
