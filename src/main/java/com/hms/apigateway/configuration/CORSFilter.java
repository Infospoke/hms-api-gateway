package com.hms.apigateway.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@Order(-1)
public class CORSFilter implements WebFilter {

	private static final String ALLOWED_ORIGIN = "http://localhost:4200";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

		HttpHeaders headers = exchange.getResponse().getHeaders();

		headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
		headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		headers.set("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Channel");
		headers.add("Access-Control-Allow-Credentials", "true");
		headers.add("Access-Control-Expose-Headers", "Cookie");
		headers.add("Strict-Transport-Security", "max-age=36500 ; includeSubDomains ; preload");
		headers.add("Content-Security-Policy",
				"default-src 'self' https:; font-src 'self' https: data:; img-src 'self' https: data:; object-src 'none'; script-src https:; style-src 'self' https: 'unsafe-inline'");

		if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
			exchange.getResponse().setStatusCode(HttpStatus.OK);
			return exchange.getResponse().setComplete();
		}

		return chain.filter(exchange);
	}
}