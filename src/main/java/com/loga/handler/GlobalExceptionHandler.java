package com.loga.handler;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(ConversionFailedException.class)
	public ResponseEntity<Object> BadRequestHandler() {
		return ResponseEntity.ok("Bad request");
	}

}
