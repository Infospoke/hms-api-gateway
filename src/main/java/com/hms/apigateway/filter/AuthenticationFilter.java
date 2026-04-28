package com.hms.apigateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.hms.apigateway.utils.JwtService;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
@Order(0)
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
	private JwtService jwtService;

	public AuthenticationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {

			List<String> publicPaths = List.of("/hms/login/user-login", "/hms/login/forgot-password");

			String path = exchange.getRequest().getURI().getPath();

			boolean isPublic = publicPaths.stream().anyMatch(path::startsWith);

			if (isPublic) {
				return chain.filter(exchange);
			}

			String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
			}

			String token = authHeader.substring(7);

			try {
				jwtService.validateToken(token);
				return chain.filter(exchange);

			} catch (Exception ex) {
				return onError(exchange, ex.getMessage(), HttpStatus.UNAUTHORIZED);
			}
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {

		exchange.getResponse().setStatusCode(status);
		exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

		String body = String.format("""
				{
				  "status": "FAILURE",
				  "message": "%s"
				}
				""", message);

		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));

		return exchange.getResponse().writeWith(Mono.just(buffer));
	}

	public static class Config {
	}
}