package com.example.backend.global.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * SwaggerConfig
 * 스웨거 api 문서화 관련 설정
 * @author 100minha
 */
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info().title("2차 프로젝트 12팀 API").version("v1"))
			// 보안 요구사항 설정 (Bearer 토큰 대신 쿠키 사용)
			.addSecurityItem(new SecurityRequirement().addList("cookieAuth"))
			.components(new io.swagger.v3.oas.models.Components()
				.addSecuritySchemes("cookieAuth", new SecurityScheme()
					.type(SecurityScheme.Type.APIKEY)
					.in(SecurityScheme.In.COOKIE)  // 요청 시 쿠키를 자동으로 포함
					.name("accessToken"))); // 쿠키 이름
	}

	@Bean
	public GroupedOpenApi integrationApi() {
		return GroupedOpenApi.builder()
			.group("api")
			.pathsToMatch("/**")
			.build();
	}

	@Bean
	public GroupedOpenApi adminApi() {
		return GroupedOpenApi.builder()
			.group("adminApi")
			.pathsToMatch("/admin/**")
			.build();
	}

	@Bean
	public GroupedOpenApi categoryApi() {
		return GroupedOpenApi.builder()
			.group("categoryApi")
			.pathsToMatch("/categories/**")
			.build();
	}

	@Bean
	public GroupedOpenApi groupApi() {
		return GroupedOpenApi.builder()
			.group("groupApi")
			.pathsToMatch("/groups/**")
			.build();
	}

	@Bean
	public GroupedOpenApi memberApi() {
		return GroupedOpenApi.builder()
			.group("memberApi")
			.pathsToMatch("/member/**")
			.build();
	}

	@Bean
	public GroupedOpenApi voteApi() {
		return GroupedOpenApi.builder()
			.group("voteApi")
			.pathsToMatch("/votes/**")
			.build();
	}

	@Bean
	public GroupedOpenApi voterApi() {
		return GroupedOpenApi.builder()
			.group("voterApi")
			.pathsToMatch("/voters/**")
			.build();
	}

	@Bean
	public GroupedOpenApi authApi() {
		return GroupedOpenApi.builder()
			.group("authApi")
			.pathsToMatch("/auth/**")
			.build();
	}
}
