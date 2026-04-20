package com.hms.apigateway.exceptions;

@SuppressWarnings("serial")
public class UnauthorizedException extends RuntimeException{
	public UnauthorizedException(String message) {
        super(message);
    }

}
