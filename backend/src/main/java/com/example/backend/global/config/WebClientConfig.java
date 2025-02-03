package com.example.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClientConfig
 * WebClient 스프링 빈 등록 클래스
 * @author 100minha
 */
@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}
}
