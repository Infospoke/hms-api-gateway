package com.hms.apigateway.configuration;

import java.util.Arrays;
import java.util.List;

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

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost:4200",
            "http://172.16.1.101:4400",
            "http://localhost:5173",
            "http://172.16.1.101:5173"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/ws")) {
            return chain.filter(exchange);
        }

        HttpHeaders headers = exchange.getResponse().getHeaders();
        String requestOrigin = exchange.getRequest().getHeaders().getOrigin();

        if (requestOrigin != null && ALLOWED_ORIGINS.contains(requestOrigin)) {
            headers.set("Access-Control-Allow-Origin", requestOrigin);
        }

        headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.set("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Channel");
        headers.set("Access-Control-Allow-Credentials", "true");
        headers.set("Access-Control-Expose-Headers", "Cookie");
        headers.set("Strict-Transport-Security", "max-age=36500 ; includeSubDomains ; preload");
        headers.set("Content-Security-Policy",
                "default-src 'self' https:; font-src 'self' https: data:; img-src 'self' https: data:; object-src 'none'; script-src https:; style-src 'self' https: 'unsafe-inline'");

        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}


//package com.hms.apigateway.configuration;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//
//import reactor.core.publisher.Mono;
//
//@Configuration
//@Order(-1)
//public class CORSFilter implements WebFilter {
//
//	private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
//	        "http://localhost:4200",
//	        "http://172.16.1.101:4400"
//	);
//
//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//
//	    HttpHeaders headers = exchange.getResponse().getHeaders();
//	    String requestOrigin = exchange.getRequest().getHeaders().getOrigin();
//
//	    if (requestOrigin != null && ALLOWED_ORIGINS.contains(requestOrigin)) {
//	        headers.set("Access-Control-Allow-Origin", requestOrigin);
//	    }
//
//	    headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//	    headers.set("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Channel");
//	    headers.set("Access-Control-Allow-Credentials", "true");
//	    headers.set("Access-Control-Expose-Headers", "Cookie");
//	    headers.set("Strict-Transport-Security", "max-age=36500 ; includeSubDomains ; preload");
//	    headers.set("Content-Security-Policy",
//	            "default-src 'self' https:; font-src 'self' https: data:; img-src 'self' https: data:; object-src 'none'; script-src https:; style-src 'self' https: 'unsafe-inline'");
//
//	    if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
//	        exchange.getResponse().setStatusCode(HttpStatus.OK);
//	        return exchange.getResponse().setComplete();
//	    }
//
//	    return chain.filter(exchange);
//	}
//}