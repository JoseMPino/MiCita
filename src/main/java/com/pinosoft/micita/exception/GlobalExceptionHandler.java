package com.pinosoft.micita.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
	 @ExceptionHandler(CitaException.class)
	    public ResponseEntity<Object> handleCitaException(CitaException ex, WebRequest request) {
	        Map<String, Object> body = new LinkedHashMap<>();
	        body.put("timestamp", LocalDateTime.now());
	        body.put("errorType", ex.getErrorType().name());
	        body.put("message", ex.getMessage());
	        body.put("status", ex.getHttpStatus().value());
	        body.put("error", ex.getHttpStatus().getReasonPhrase());
	        
	        return new ResponseEntity<>(body, ex.getHttpStatus());
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
	        Map<String, Object> body = new LinkedHashMap<>();
	        body.put("timestamp", LocalDateTime.now());
	        body.put("errorType", CitaException.ErrorType.INTERNAL_ERROR.name());
	        body.put("message", "Ocurrió un error inesperado");
	        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
	        body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	        
	        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

}
