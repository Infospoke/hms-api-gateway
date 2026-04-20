package com.hms.apigateway.configuration;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.hms.service.enums.ChannelTypes;

import reactor.core.publisher.Mono;

@Order(1)
@Component
public class ChannelValidator implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

		String channel = exchange.getRequest().getHeaders().getFirst("X-Channel");

		if (channel == null || channel.trim().isEmpty()) {
			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			return exchange.getResponse().setComplete();
		}
		boolean valid = channel.equalsIgnoreCase(ChannelTypes.WEB.getChannelName())
				|| channel.equalsIgnoreCase(ChannelTypes.MOBILE.getChannelName());

		if (!valid) {
			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			return exchange.getResponse().setComplete();
		}
		return chain.filter(exchange);
	}
}