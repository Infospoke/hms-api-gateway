package com.hms.apigateway.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler(MissingValueException.class)
	public Mono<ErrorResponse> handleMissing(MissingValueException ex, ServerWebExchange exchange) {

		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

		return Mono.just(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}

	@ExceptionHandler(InvalidValueException.class)
	public Mono<ErrorResponse> handleInvalid(InvalidValueException ex, ServerWebExchange exchange) {

		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

		return Mono.just(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}

	@ExceptionHandler(Exception.class)
	public Mono<ErrorResponse> handleGeneric(Exception ex, ServerWebExchange exchange) {

		exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

		return Mono.just(new ErrorResponse("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}
}
