package com.hms.apigateway.configuration;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import feign.codec.Decoder;
import feign.codec.Encoder;
@Configuration
public class SecurityConfiguration {

	@Bean
	public Decoder feignDecoder() {
		return new SpringDecoder(() -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter()));
	}

	@Bean
	public Encoder feignEncoder() {
		return new SpringEncoder(() -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter()));
	}

}
