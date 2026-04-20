package com.hms.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.hms.apigateway.exceptions.UnauthorizedException;
import com.hms.apigateway.external.service.ExternalServiecFeignClient;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
	private RouteValidator validator;

	private ExternalServiecFeignClient externalServiecFeignClient;

	public AuthenticationFilter(@Lazy ExternalServiecFeignClient externalServiecFeignClient) {
		super(Config.class);
		this.externalServiecFeignClient = externalServiecFeignClient;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			if (validator.isSecured.test(exchange.getRequest())) {
				// header contains token or not
				System.out.println("******************" + validator.isSecured + "******************");
				System.out.println("******************" + exchange.getRequest());
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				    return exchange.getResponse().setComplete();
				}
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7);
				}
				boolean validateToken = externalServiecFeignClient.validateToken(authHeader);

				if (!validateToken) {
				    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				    return exchange.getResponse().setComplete();
				}
			}
			return chain.filter(exchange);
		});
	}

	public static class Config {

	}
}
