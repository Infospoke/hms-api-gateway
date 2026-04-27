package com.hms.apigateway.configuration;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.apigateway.exceptions.ErrorResponse;
import com.hms.service.enums.ChannelTypes;

import reactor.core.publisher.Mono;

@Order(1)
@Component
public class ChannelValidator implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String channel = exchange.getRequest().getHeaders().getFirst("X-Channel");

        if (channel == null || channel.trim().isEmpty()) {
            return buildErrorResponse(exchange, "X-Channel header is missing");
        }

        boolean valid = channel.equalsIgnoreCase(ChannelTypes.WEB.getChannelName())
                || channel.equalsIgnoreCase(ChannelTypes.MOBILE.getChannelName());

        if (!valid) {
            return buildErrorResponse(exchange, "Invalid X-Channel value. Allowed values: WEB, MOBILE");
        }

        return chain.filter(exchange);
    }

    private Mono<Void> buildErrorResponse(ServerWebExchange exchange, String message) {

        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

        ErrorResponse error = new ErrorResponse(message, HttpStatus.BAD_REQUEST.value());

        byte[] bytes;
        try {
            bytes = new ObjectMapper().writeValueAsBytes(error);
        } catch (Exception e) {
            bytes = message.getBytes();
        }

        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(bytes);

        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}