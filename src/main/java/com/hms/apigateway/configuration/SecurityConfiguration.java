package com.hms.apigateway.configuration;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import feign.codec.Decoder;
import feign.codec.Encoder;
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

	@Bean
	public Decoder feignDecoder() {
		return new SpringDecoder(() -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter()));
	}

	@Bean
	public Encoder feignEncoder() {
		return new SpringEncoder(() -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter()));
	}
	
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

	    return http
	            .csrf(ServerHttpSecurity.CsrfSpec::disable)
	            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
	            .authorizeExchange(exchanges -> exchanges
	                    .pathMatchers("/hms/login/user-login",
	                            "/hms/login/forgot-password").permitAll()
	                    .anyExchange().permitAll()
	            )
	            .build();
	}

}
