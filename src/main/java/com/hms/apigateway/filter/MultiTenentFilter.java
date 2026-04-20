package com.hms.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class MultiTenentFilter extends AbstractGatewayFilterFactory<MultiTenentFilter.Config>{

	public MultiTenentFilter() {
        super(Config.class);
    }
	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
            // Extract tenant information from the request
            String tenantId = exchange.getRequest().getHeaders().getFirst("Xtenant");
            if (tenantId == null) {
                // Optionally, you can set a default tenant or return an error
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                return exchange.getResponse().setComplete();
            }
            // Add tenant information to the request headers for forwarding to microservices
            exchange.getRequest().mutate().header("Xtenant", tenantId);
            return chain.filter(exchange);
        };
	}
	
	public static class Config {
        // Configuration options can be added here
    }
}

