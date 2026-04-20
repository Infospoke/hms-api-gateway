package com.hms.apigateway.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
	
	private String exceptionMsg;
	private Integer httpStatusCode;
	
	
	
}

